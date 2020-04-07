package cn.wis.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.wis.account.model.entity.Page;
import cn.wis.account.model.table.Role;

public interface RoleMapper extends BaseMapper<Role> {

	@Results(id = "roleMap", value = {
			@Result(column = "id", property = "id", jdbcType = JdbcType.VARCHAR),
			@Result(column = "creator_id", property = "creatorId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.BIGINT),
			@Result(column = "updater_id", property = "updaterId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.BIGINT),
			@Result(column = "maximum", property = "maximum", jdbcType = JdbcType.INTEGER),
			@Result(column = "appellation", property = "appellation", jdbcType = JdbcType.VARCHAR),
			@Result(column = "description", property = "description", jdbcType = JdbcType.VARCHAR),
	})
	@Select({"SELECT * FROM role "
			+ "WHERE appellation = #{name}"})
	Role selectByName(@Param("name") String name);

	@Select({"SELECT COUNT(*) AS total FROM role"})
	@Results(id = "roleTotalMap", value = {
			@Result(column = "total", javaType = Integer.class)
	})
	Integer countAll();

	@Select({"<script>"
			+ "SELECT SQL_CALC_FOUND_ROWS * FROM role "
			+ "<if test='page.keyWord != null'>"
			+ "WHERE appellation LIKE CONCAT('%', #{page.keyWord}, '%') "
			+ "</if>"
			+ "LIMIT ${(page.index - 1) * page.count}, #{page.count};"
			+ "SELECT FOUND_ROWS() AS total;"
			+ "</script>"})
	@ResultMap({"roleMap", "roleTotalMap"})
	List<List<?>> selectByPage(@Param("page") Page page);

}
