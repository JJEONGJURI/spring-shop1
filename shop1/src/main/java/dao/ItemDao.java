package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import logic.Item;

@Repository
//@Repository(연속객체) = @Component(객체생성) + dao 기능(데이터베이스 연결)
//spring-mvc.xml 에서 <context:component-scan base-package="controller,logic,dao"> 때문에 전부 객체 생성 가능
public class ItemDao {
	private NamedParameterJdbcTemplate template;
	private Map<String,Object> param = new HashMap<>(); //파라미터 전송할 때 쓸거임
	private RowMapper<Item> mapper = 
			new BeanPropertyRowMapper<>(Item.class);
	//db에서 조회된 컬럼명과 item 클래스의 프로퍼티를 비교해서 같은 값을 Item 객체로 생성
	
	@Autowired
	//spring-db.xml 에서 설정된 dataSource 객체 주입
	public void setDataSource(DataSource dataSource) {
		//dataSource : db에 연결된 객체.
		template = new NamedParameterJdbcTemplate(dataSource);
		// NamedParameterJdbcTemplate : spring 프레임워크의 jdbc 템플릿
	}
	public List<Item> list() {
		return template.query("select * from item order by id", param, mapper);
		//"select * from item order by id" 조회된 결과를 mapper 로 만들어서 Item.class 넣는다
	}

}
