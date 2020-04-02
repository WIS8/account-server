package cn.wis.account.mapper;

import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.wis.account.model.table.Authority;

public interface AuthorityMapper extends BaseMapper<Authority> {

	default List<Authority> selectAllByApis(List<String> apiIds) {
		if (apiIds.isEmpty()) {
			return Collections.emptyList();
		}
		return selectList(new LambdaQueryWrapper<Authority>().in(Authority::getApiId, apiIds));
	}

}
