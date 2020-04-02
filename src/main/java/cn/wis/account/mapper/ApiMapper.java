package cn.wis.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.wis.account.model.table.Api;

public interface ApiMapper extends BaseMapper<Api> {

	@Select({"SELECT COUNT(id) FROM api "
			+ "WHERE app_id = #{appId}"})
	int countByApp(@Param("appId") String appId);

	@Select({"SELECT * FROM api "
			+ "WHERE app_id = #{appId}"})
	List<Api> selectAllByApp(@Param("appId") String appId);

	@Insert({"<script> "
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
            + "</foreach> "
            + "</script>"})
	int batchInsert(@Param("list") List<Api> apis);

}
