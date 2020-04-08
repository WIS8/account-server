package cn.wis.account.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.wis.account.mapper.ParamValueMapper;
import cn.wis.account.mapper.ParameterMapper;
import cn.wis.account.model.entity.Page;
import cn.wis.account.model.request.param.ParamUpdateRequest;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.model.table.ParamValue;
import cn.wis.account.model.table.Parameter;
import cn.wis.account.model.table.Provider;
import cn.wis.account.model.vo.PageVo;
import cn.wis.account.model.vo.ParamVo;
import cn.wis.account.service.ParameterService;
import cn.wis.account.support.ApplicationSup;
import cn.wis.account.support.ProviderSup;
import cn.wis.account.util.ResultHelper;

@Service
public class ParameterServiceImpl implements ParameterService {

	@Resource
	private HttpServletRequest http;

	@Resource
	private ParameterMapper paramMapper;

	@Resource
	private ParamValueMapper paramValueMapper;

	@Resource
	private ApplicationSup appSup;

	@Resource
	private ProviderSup providerSup;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(ParamUpdateRequest request) {
		Parameter param = new Parameter();
		param.setId(request.getId());
		param.setDefaultValue(request.getValue());
		param.setDescription(param.getDescription());
		param.setUpdateFields(getLoginMember(http).getId());
		if (paramMapper.updateById(param) != 1) {
			throw new ServiceException(ResultEnum.UPDATE_ERROR);
		}
	}

	@Override
	public Map<String, String> confirm(String providerId) {
		Provider provider = providerSup.checkProviderId(providerId);
		List<Parameter> parameters = paramMapper.selectAllInApp(provider.getAppId());
		if (CollectionUtil.isEmpty(parameters)) {
			return new HashMap<String, String>();
		}
		List<ParamValue> values = paramValueMapper.selectAllInProvider(providerId);
		if (CollectionUtil.isEmpty(values)) {
			return parameters.parallelStream().collect(Collectors
					.toMap(Parameter::getAppellation, Parameter::getDefaultValue));
		}
		final Map<String, String> result = new HashMap<String, String>();
		Map<String, Parameter> map = parameters.parallelStream()
				.collect(Collectors.toMap(Parameter::getId, param -> param));
		map.forEach((key, value) -> result.put(value.getAppellation(), value.getDefaultValue()));
		values.stream().forEach(value -> result.put(map.get(value.getParamId()).getAppellation(), value.getContent()));
		return result;
	}

	@Override
	public PageVo search(Page page) {
		return ResultHelper.translate(paramMapper.selectByPage(page), this::getParamVo);
	}

	private ParamVo getParamVo(Parameter param) {
		ParamVo vo = new ParamVo();
		BeanUtil.copyProperties(param, vo);
		vo.setKey(param.getAppellation());
		vo.setValue(param.getDefaultValue());
		vo.setAppName(appSup.checkAppIdInCache(param.getAppId()).getAppellation());
		vo.setUniqueParamNumber(paramValueMapper.countByParam(param.getId()));
		return vo;
	}

}
