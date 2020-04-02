package cn.wis.account.support;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.wis.account.mapper.ProviderMapper;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.model.table.Provider;
import cn.wis.account.util.ResultHelper;

@Component
public class ProviderSup {

	@Resource
	private ProviderMapper providerMapper;

	public Provider checkProviderId(String providerId) {
		Provider provider = providerMapper.selectById(providerId);
		if (ResultHelper.notAn(provider)) {
			throw new ServiceException(ResultEnum.NO_KEY_ERROR, "providerId:" + providerId);
		}
		return provider;
	}

}
