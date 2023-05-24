package dao;

import java.util.HashMap;
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

import logic.User;


@Repository
public class UserDao {
	private NamedParameterJdbcTemplate template;
	private RowMapper<User> mapper = new BeanPropertyRowMapper<User>(User.class);
	private Map<String,Object> param = new HashMap<>();
	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	public void insert(@Valid User user) { //화면에서 입력받은 정보를 저장하고 있는 user 객체
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		//param : user  객체의 프로퍼티를 이용하여 db에 값 등록
		String sql = "insert into useraccount (userid,username,password,"
				+ " birthday,phoneno,postcode,address,email) values " //birthday는 datetime type
				+ " (:userid,:username,:password,"
				+ "  :birthday,:phoneno,:postcode,:address,:email)"; //birthday는 date type
		template.update(sql, param);
				
	}
	public User selectOne(String userid) {
		param.clear();
		param.put("userid", userid);
		return template.queryForObject
				("select * from useraccount where userid=:userid", param, mapper);
		//mapper 에 설정된 유저 프로퍼티와 같은거?
	}
	public User selectOne(String userid, String password) {
		param.clear();
		param.put("userid", userid);
		param.put("password", password);
		return template.queryForObject
				("select * from useraccount where userid=:userid and password=:password", param, mapper);
	}
	public void update(@Valid User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String sql = 
				"update useraccount set username=:username,birthday=:birthday,phoneno=:phoneno,"
				+ "	postcode=:postcode,address=:address,email=:email"
				+ " where userid=:userid";
		template.update(sql, param);
		
	}
	public void delete(@Valid User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String sql = 
				"delete from useraccount where userid=:userid";
		template.update(sql,param);
		
	}
	public void delete(String userid) {
		param.clear();
		param.put("userid", userid);
		template.update("delete from useraccount where userid=:userid", param);

	}
	public void update(String userid, String chgpass) {
		param.clear();
		param.put("userid", userid);
		param.put("password", chgpass);
		template.update("update useraccount set password=:password where userid=:userid",param);
		
	}

}

