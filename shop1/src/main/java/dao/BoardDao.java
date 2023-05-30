package dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

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
}
