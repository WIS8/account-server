package cn.wis.account.mapper;

import java.util.Collections;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.wis.account.model.table.Authority;

public interface AuthorityMapper extends BaseMapper<Authority> {

	@Select({"SELECT * FROM authority "
			+ "WHERE role_id = #{roleId} "
			+ "AND api_id = #{apiId}"})
	Authority selectByRoleAndApi(@Param("roleId") String roleId, @Param("apiId") String apiId);

	@Delete({"DELETE FROM authority "
			+ "WHERE role_id = #{roleId} "
			+ "AND api_id = #{apiId}"})
	int deleteByRoleAndApi(@Param("roleId") String roleId, @Param("apiId") String apiId);

	default List<Authority> selectAllByApis(List<String> apiIds) {
		if (apiIds.isEmpty()) {
			return Collections.emptyList();
		}
		return selectList(new LambdaQueryWrapper<Authority>().in(Authority::getApiId, apiIds));
	}

	default List<Authority> selectAllByApi(String apiId) {
		return selectList(new LambdaQueryWrapper<Authority>().eq(Authority::getApiId, apiId));
	}

}
