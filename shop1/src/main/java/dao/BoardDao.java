package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Board;

@Repository
public class BoardDao {
	private NamedParameterJdbcTemplate template;
	private Map<String,Object> param = new HashMap<>(); //파라미터 전송할 때 쓸거임
	private RowMapper<Board> mapper = 
			new BeanPropertyRowMapper<>(Board.class);
	//db에서 조회된 컬럼명과 item 클래스의 프로퍼티를 비교해서 같은 값을 Item 객체로 생성
	private String select = "select num, writer, pass, title, content, file1 fileurl, "
			+ " regdate, readcnt, grp, grplevel, grpstep, boardid from board";
	//file1 은 fileurl 로 별명 주
	
	@Autowired
	//spring-db.xml 에서 설정된 dataSource 객체 주입
	public void setDataSource(DataSource dataSource) {
			template = new NamedParameterJdbcTemplate(dataSource);
	}

	public int maxNum() {
		return template.queryForObject("select ifnull(max(num),0) from board",param, Integer.class);
	
	}

	public void insert(Board board) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(board);
		//board객체에 있는 파라미터를 내가 프로퍼티로 쓸거임
		String sql = "insert into board (num, boardid, writer, pass, title, content, file1, regdate, readcnt, grp, grplevel, grpstep) "
				+ " values (:num, :boardid, :writer, :pass, :title, :content, :fileurl, now(), 0 ,:grp, :grplevel, :grpstep)";
		//file1에는 fil1url 넣어라
		//조회수는 0 
		template.update(sql, param);
	}

	public int count(String boardid, String searchtype, String searchcontent) {
		String sql = "select count(*) from board where boardid=:boardid"; //boardid 에 있는 레코드건수 가져와
		param.clear();
		param.put("boardid",boardid);
		
		//null이 아니라면 밑에거 추가
		if(searchtype != null && searchcontent != null) { //검색요청
			//검색값을 가지고 있으면
			sql += " and " + searchtype + " like :searchcontent";
			//searchtype은 컬럼명임
			param.put("searchcontent","%"+searchcontent + "%");
		}
		return template.queryForObject(sql,param,Integer.class); 
	}

	public List<Board> list(Integer pageNum, int limit, String boardid, String searchtype, String searchcontent) {
		param.clear();
		String sql = select;
		sql+=" where boardid=:boardid";
		if(searchtype != null && searchcontent != null) { //검색요청
			sql += " and " + searchtype + " like :searchcontent";
			param.put("searchcontent", "%"+searchcontent +"%");
		}
		
		sql += " order by grp desc, grpstep asc limit :startrow, :limit";
		//limit 만큼만 가져와
		param.put("startrow", (pageNum-1) * limit); 
		//1페이지 : 0, 2페이지 : 10
		param.put("limit", limit);
		param.put("boardid", boardid);
		//해당 보드거만 가져옴
		return template.query(sql, param,mapper);
		//mapper : 보드 객체 안의 file1은 문자열값이 아니고 multipart 로 넣어줌? fileurl 로 들어감
	}

	public Board selectOne(Integer num) {
		String sql = select + " where num = :num";
		param.clear();
		param.put("num", num);
		return template.queryForObject(sql, param, mapper);
	}

	public void addReadcnt(Integer num) {
		param.clear();
		param.put("num", num);
		String sql = "update board set readcnt = readcnt + 1 where num=:num";
		template.update(sql, param);
		
	}

//	public int grpStepAdd() {
//		return template.queryForObject("select ifnull(max(grpstep),0) from board where num=:num",param, Integer.class);
//	}

//	public void rinsert(Board board) {
//		SqlParameterSource param = new BeanPropertySqlParameterSource(board);
//		//board객체에 있는 파라미터를 내가 프로퍼티로 쓸거임
//		String sql = "insert into board (num, boardid, writer, pass, title, content, file1, regdate, readcnt, grp, grplevel, grpstep) "
//				+ " values (:num, :boardid, :writer, :pass, :title, :content, :fileurl, now(), 0 ,:grp, :grplevel+1, :grpstep)";
//		//file1에는 fil1url 넣어라
//		//조회수는 0 
//		template.update(sql, param);
//	}

	public void updateGrpStep(@Valid Board board) {
		String sql = "update board set grpstep=grpstep + 1"
				+ " where grp = :grp and grpstep > :grpstep";
		param.clear();
		param.put("grp", board.getGrp()); //원글의 grp
		param.put("grpstep", board.getGrpstep()); //원글의 grpstep
		template.update(sql, param);
	}




}
