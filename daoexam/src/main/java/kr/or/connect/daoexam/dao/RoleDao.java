package kr.or.connect.daoexam.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import kr.or.connect.daoexam.dto.Role;
import static kr.or.connect.daoexam.dao.RoleDaoSqls.*; // 미리 정의한 SQL을 사용하기 위함 (static import)

@Repository
public class RoleDao 
{
	/** SELECT_ALL */
	// 쿼리를 실행하기 위한 객체
	private NamedParameterJdbcTemplate jdbc; // ? 대신 이름으로 바인딩 가능 (결과값 가져올 수도 있음)
	private RowMapper<Role> rowMapper = BeanPropertyRowMapper.newInstance(Role.class);
	// BeanPropertyRowMapper객체를 통해 속성의 값을 자동으로 DTO에 담아주게 됨
	// DBMS에서는 단어와 단어를 구분할 때 언더바 사용 - ex) role_id
	// Java에서는 단어와 단어를 구분할 때 카멜 표기법을 사용 - ex) roleId
	// BeanPropertyRowMapper는 DBMS와 Java의 이름규칙을 맞춰주는 기능을 지님
	
	/** INSERT */
	private SimpleJdbcInsert insertAction;
	
	/** COMMON */
	public RoleDao(DataSource dataSource)  
	{
		// Spring 버전 4.3부터는 ComponentScan으로 객체를 찾았을 때 기본 생성자가 없다면 자동으로 객체 주입
		
		/** SELECT_ALL, UPDATE, SELECT_BY_ID, DELETE_BY_ID */
		this.jdbc = new NamedParameterJdbcTemplate(dataSource); // 객체생성
		
		/** INSERT */
		this.insertAction = new SimpleJdbcInsert(dataSource).withTableName("role"); // 어떤 테이블에 넣을지
	}
	
	/** SELECT_ALL */
	public List<Role> selectAll()
	{
		return jdbc.query(SELECT_ALL, Collections.emptyMap(), rowMapper); // 비어있는 map객체를 하나 선언
		// static import를 쓰면 패키지 명 없이 static 멤버를 사용할 수 있음
		// SQL문에 바인딩 할 것이 있는 경우 바인딩할 값을 전달할 목적으로 사용하는 객체
		// SELECT 건 당 정보를 DTO에 저장하는 목적으로 사용
		// query() : 결과가 여러 건이었을 때 내부적으로 반복하면서 DTO를 생성, 그 결과를 리스트에 담아줌
	}
	
	/** INSERT */
	public int insert(Role role) 
	{
		// INSERT문 같은 경우, PK를 자동으로 생성해야 하는 경우도 존재 
		// 생성된 PK값을 다시 읽어오는 부분 필요  (SimpleJdbcInsert 객체가 대신 수행)
		// 일단 여기서는 PK값을 우리가 직접 넣음
		
		// Role객체에 있는 값을 Map으로 바꿔줌
		SqlParameterSource params = new BeanPropertySqlParameterSource(role);
		
		// Map을 전달시 값을 알아서 저장(insert)
		return insertAction.execute(params); 
	}

	/** UPDATE */
	public int update(Role role) // 인자로 받은 role이라는 DTO객체는 바인딩이 필요한 부분에 들어갈 것
	{
		SqlParameterSource params = new BeanPropertySqlParameterSource(role); 
		// BeanPropertySqlParameterSource가 Map으로 바꿔주는 역할을 수행
		return jdbc.update(UPDATE, params); // 첫째인자: SQL, 두번째인자: Map객체
		// params는 바인딩이 필요한 부분에 매핑시켜줄 수 있는 객체라고 이해하면 됨
	}
	
	/** DELETE_BY_ID */
	public int deleteById(Integer id) 
	{
		Map<String, ?> params = Collections.singletonMap("roleId", id);
		// BeanPropertySqlParameterSource 대신 위의 방법을 사용해도 좋음 -> 단일값 인자인 경우
		// singletonMap의 경우 값이 딱 한 건만 들어갈 때 사용
		return jdbc.update(DELETE_BY_ROLE_ID, params); // UPDATE와 비슷
	}
	
	/** SELECT_BY_ID */
	public Role selectById(Integer id) 
	{
		try 
		{
			Map<String, ?> params = Collections.singletonMap("roleId", id);
			return jdbc.queryForObject(SELECT_BY_ROLE_ID, params, rowMapper);
			// queryForObject()를 사용 : [쿼리문, roleId값, rowMapper]
		}
		catch(EmptyResultDataAccessException e) // 조건에 맞는 값이 없는 경우
		{
			// 주의해서 사용
			return null; 
		} 
	}
}