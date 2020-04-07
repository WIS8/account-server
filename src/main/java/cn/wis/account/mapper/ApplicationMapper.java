package cn.wis.account.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.wis.account.model.entity.Page;
import cn.wis.account.model.table.Application;

public interface ApplicationMapper extends BaseMapper<Application> {

	@Results(id = "appMap", value = {
			@Result(column = "id", property = "id", jdbcType = JdbcType.VARCHAR),
			@Result(column = "creator_id", property = "creatorId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.BIGINT),
			@Result(column = "updater_id", property = "updaterId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.BIGINT),
			@Result(column = "plugin_name", property = "pluginName", jdbcType = JdbcType.VARCHAR),
			@Result(column = "appellation", property = "appellation", jdbcType = JdbcType.VARCHAR),
			@Result(column = "description", property = "description", jdbcType = JdbcType.VARCHAR),
	})
	@Select({"SELECT * FROM application "
			+ "WHERE appellation = #{name}"})
	Application selectByName(@Param("name") String name);

	@Results(id = "appTotalMap", value = {
			@Result(column = "total", jdbcType = JdbcType.INTEGER)
	})
	@Select({"SELECT COUNT(id) AS total FROM application"})
	int countAll();

	@Select({"<script>"
			+ "SELECT id, plugin_name, description FROM application "
			+ "WHERE id IN "
			+ "<foreach collection='set' item='item' open='(' separator=',' close=')'>"
			+ "#{item}"
			+ "</foreach>"
			+ "</script>"})
	List<Application> selectAllByIds(@Param("set") Set<String> ids);

	@Select({"<script>"
			+ "SELECT SQL_CALC_FOUND_ROWS * FROM application "
			+ "<if test='page.keyWord != null'>"
			+ "WHERE appellation LIKE CONCAT('%', #{page.keyWord}, '%') "
			+ "</if>"
			+ "LIMIT ${(page.index - 1) * page.count}, #{page.count};"
			+ "SELECT FOUND_ROWS() AS total;"
			+ "</script>"})
	@ResultMap({"appMap", "appTotalMap"})
	List<List<?>> selectByPage(@Param("page") Page page);

}
