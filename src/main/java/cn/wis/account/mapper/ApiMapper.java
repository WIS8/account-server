package cn.wis.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.wis.account.model.entity.Page;
import cn.wis.account.model.table.Api;

public interface ApiMapper extends BaseMapper<Api> {

	@Insert({"<script>"
            + "INSERT INTO api "
            + "(id, app_id, router, access_rule, appellation, description, creator_id, create_time) VALUES "
            + "<foreach collection='list' item='item' index='index' separator=','> "
            + "(#{item.id, jdbcType=VARCHAR}, "
            + "#{item.appId, jdbcType=VARCHAR}, "
            + "#{item.router, jdbcType=VARCHAR}, "
            + "#{item.accessRule, jdbcType=INTEGER}, "
            + "#{item.appellation, jdbcType=VARCHAR}, "
            + "#{item.description, jdbcType=VARCHAR}, "
            + "#{item.creatorId, jdbcType=VARCHAR}, "
            + "#{item.createTime, jdbcType=BIGINT}) "
            + "</foreach>"
            + "</script>"})
	int batchInsert(@Param("list") List<Api> apis);

	@Select({"SELECT COUNT(id) AS total FROM api "
			+ "WHERE app_id = #{appId}"})
	@Results(id = "apiTotalMap", value = {
			@Result(column = "total", jdbcType = JdbcType.INTEGER)
	})
	int countByApp(@Param("appId") String appId);

	@Results(id = "apiMap", value = {
			@Result(column = "id", property = "id", jdbcType = JdbcType.VARCHAR),
			@Result(column = "creator_id", property = "creatorId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.BIGINT),
			@Result(column = "updater_id", property = "updaterId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.BIGINT),
			@Result(column = "app_id", property = "appId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "router", property = "router", jdbcType = JdbcType.VARCHAR),
			@Result(column = "access_rule", property = "accessRule", jdbcType = JdbcType.INTEGER),
			@Result(column = "appellation", property = "appellation", jdbcType = JdbcType.VARCHAR),
			@Result(column = "description", property = "description", jdbcType = JdbcType.VARCHAR),
	})
	@Select({"SELECT * FROM api "
			+ "WHERE app_id = #{appId}"})
	List<Api> selectAllByApp(@Param("appId") String appId);

	@Select({"SELECT id, app_id, access_rule FROM api "
			+ "WHERE id IN ("
			+ "SELECT api_id FROM authority "
			+ "WHERE role_id = #{roleId})"})
	List<Api> selectAllByRole(@Param("roleId") String roleId);

	@Select({"<script>"
            + "SELECT DISTINCT app_id FROM api "
            + "WHERE access_rule != #{code} "
            + "<if test='list != null and list.size != 0'>"
            + "AND id NOT IN "
            + "<foreach collection='list' item='item' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</if>"
            + "</script>"})
	List<Api> selectAllNotInRuleOrApi(@Param("list") List<String> apiIds, @Param("code") int code);

	@Select({"<script>"
			+ "SELECT SQL_CALC_FOUND_ROWS * FROM api "
			+ "<if test='page.keyWord != null'>"
			+ "WHERE appellation LIKE CONCAT('%', #{page.keyWord}, '%') "
			+ "</if>"
			+ "LIMIT ${(page.index - 1) * page.count}, #{page.count};"
			+ "SELECT FOUND_ROWS() AS total;"
			+ "</script>"})
	@ResultMap({"apiMap", "apiTotalMap"})
	List<List<?>> selectByPage(@Param("page") Page page);

}
