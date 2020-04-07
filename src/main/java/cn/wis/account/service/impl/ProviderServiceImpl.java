package cn.wis.account.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.bean.BeanUtil;
import cn.wis.account.component.AccountInitializer;
import cn.wis.account.mapper.ParamValueMapper;
import cn.wis.account.mapper.ProviderMapper;
import cn.wis.account.model.dto.ProviderDto;
import cn.wis.account.model.entity.Page;
import cn.wis.account.model.request.feign.ProviderConfirmRequest;
import cn.wis.account.model.request.provider.ProviderUpdateRequest;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.model.table.Application;
import cn.wis.account.model.table.Provider;
import cn.wis.account.model.vo.PageVo;
import cn.wis.account.model.vo.ProviderVo;
import cn.wis.account.service.ProviderService;
import cn.wis.account.support.ApplicationSup;
import cn.wis.account.util.ResultHelper;

@Service
public class ProviderServiceImpl implements ProviderService {

	@Resource
	private HttpServletRequest http;

	@Resource
	private ProviderMapper providerMapper;

	@Resource
	private ParamValueMapper paramValueMapper;

	@Resource
	private AccountInitializer initializer;

	@Resource
	private ApplicationSup appSup;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ProviderDto confirm(ProviderConfirmRequest request) {
		Application app = appSup.checkAppId(request.getAppId());
		Provider provider = providerMapper.selectByUrl(request.getUrl());
		if (ResultHelper.notAn(provider)) {
			provider = new Provider();
			provider.setAppId(app.getId());
			provider.setEntrance(request.getUrl());
			provider.setCreateFields(initializer.getUrl(http));
			provider.setIdentifier(app.getAppellation() + "-" + providerMapper.countByApp(app.getId()));
			if (providerMapper.insert(provider) != 1) {
				throw new RuntimeException("Fail to insert local provider into DB");
			}
		}
		ProviderDto dto = new ProviderDto();
		dto.setId(provider.getId());
		dto.setIdentifier(provider.getIdentifier());
		return dto;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(String providerId) {
		checkForeignKeyInDB(providerId);
		if (providerMapper.deleteById(providerId) != 1) {
			throw new ServiceException(ResultEnum.DELETE_ERROR);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(ProviderUpdateRequest request) {
		Provider provider = new Provider();
		provider.setId(request.getId());
		provider.setIdentifier(request.getIdentifier());
		provider.setDescription(request.getDescription());
		provider.setUpdateFields(getLoginMember(http).getId());
		if (providerMapper.updateById(provider) != 1) {
			throw new ServiceException(ResultEnum.UPDATE_ERROR);
		}
	}

	@Override
	public PageVo search(Page page) {
		return ResultHelper.translate(providerMapper.selectByPage(page), this::getProviderVo);
	}

	private void checkForeignKeyInDB(String providerId) {
		int count = paramValueMapper.countByProvider(providerId);
		if (count != 0) {
			throw new ServiceException(ResultEnum.DELETE_ERROR, "providerId存在外键：" + count);
		}
	}

	private ProviderVo getProviderVo(Provider provider) {
		ProviderVo providerVo = new ProviderVo();
		BeanUtil.copyProperties(provider, providerVo);
		providerVo.setAppName(appSup.checkAppId(provider.getId()).getAppellation());
		providerVo.setUniqueParamNumber(paramValueMapper.countByProvider(provider.getId()));
		return providerVo;
	}

}
