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
import cn.wis.account.model.table.Parameter;

public interface ParameterMapper extends BaseMapper<Parameter> {

	@Results(id = "paramTotalMap", value = {
			@Result(column = "total", javaType = Integer.class)
	})
	@Select({"SELECT COUNT(id) AS total FROM parameter "
			+ "WHERE app_id = #{appId}"})
	int countByApp(@Param("appId") String appId);

	@Results(id = "paramMap", value = {
			@Result(column = "id", property = "id", jdbcType = JdbcType.VARCHAR),
			@Result(column = "creator_id", property = "creatorId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "updater_id", property = "updaterId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.BIGINT),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.BIGINT),
			@Result(column = "app_id", property = "appId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "appellation", property = "appellation", jdbcType = JdbcType.VARCHAR),
			@Result(column = "default_value", property = "defaultValue", jdbcType = JdbcType.VARCHAR),
			@Result(column = "description", property = "description", jdbcType = JdbcType.VARCHAR),
	})
	@Select({"SELECT * FROM parameter "
			+ "WHERE app_id = #{appId}"})
	List<Parameter> selectAllInApp(@Param("appId") String appId);

	@Select({"<script>"
			+ "SELECT SQL_CALC_FOUND_ROWS * FROM parameter "
			+ "<if test='page.keyWord != null'>"
			+ "WHERE appellation LIKE CONCAT('%', #{page.keyWord}, '%') "
			+ "</if>"
			+ "LIMIT ${(page.index - 1) * page.count}, #{page.count};"
			+ "SELECT FOUND_ROWS() AS total;"
			+ "</script>"})
	@ResultMap({"paramMap", "paramTotalMap"})
	List<List<?>> selectByPage(@Param("page") Page page);

}
