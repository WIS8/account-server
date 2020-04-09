package cn.wis.account.support;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.wis.account.mapper.ApiMapper;
import cn.wis.account.model.enums.RuleListEnum;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.model.table.Api;
import cn.wis.account.util.ResultHelper;

@Component
public class ApiSup {

	@Resource
	private ApiMapper apiMapper;

	public Api checkApiId(String apiId) {
		Api api = apiMapper.selectById(apiId);
		if (ResultHelper.notAn(api)) {
			throw new ServiceException(ResultEnum.NO_KEY_ERROR, "apiId:" + apiId);
		}
		return api;
	}

	public void checkAuthApi(String apiId) {
		Api api = checkApiId(apiId);
		if (api.getAccessRule() == RuleListEnum.BLANK.getCode()) {
			throw new ServiceException(ResultEnum.ILLEGAL);
		}
	}

}
