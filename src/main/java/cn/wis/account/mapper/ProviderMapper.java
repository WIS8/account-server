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
import cn.wis.account.model.table.Provider;

public interface ProviderMapper extends BaseMapper<Provider> {

	@Results(id = "providerTotalMap", value = {
			@Result(column = "total", jdbcType = JdbcType.INTEGER)
	})
	@Select({"SELECT COUNT(id) AS total FROM provider "
			+ "WHERE app_id = #{appId}"})
	int countByApp(@Param("appId") String appId);

	@Results(id = "providerMap", value = {
			@Result(column = "id", property = "id", jdbcType = JdbcType.VARCHAR),
			@Result(column = "creator_id", property = "creatorId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.BIGINT),
			@Result(column = "updater_id", property = "updaterId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.BIGINT),
			@Result(column = "app_id", property = "appId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "identifier", property = "identifier", jdbcType = JdbcType.VARCHAR),
			@Result(column = "entrance", property = "entrance", jdbcType = JdbcType.VARCHAR),
			@Result(column = "description", property = "description", jdbcType = JdbcType.VARCHAR),
	})
	@Select({"SELECT * FROM provider "
			+ "WHERE entrance = #{url}"})
	Provider selectByUrl(@Param("url") String url);

	@Select({"<script>"
			+ "SELECT SQL_CALC_FOUND_ROWS * FROM provider "
			+ "<if test='page.keyWord != null'>"
			+ "WHERE identifier LIKE CONCAT('%', #{page.keyWord}, '%') "
			+ "</if>"
			+ "LIMIT ${(page.index - 1) * page.count}, #{page.count};"
			+ "SELECT FOUND_ROWS() AS total;"
			+ "</script>"})
	@ResultMap({"providerMap", "providerTotalMap"})
	List<List<?>> selectByPage(@Param("page") Page page);

}
