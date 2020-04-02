package cn.wis.account.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollectionUtil;
import cn.wis.account.mapper.ParamValueMapper;
import cn.wis.account.mapper.ParameterMapper;
import cn.wis.account.model.table.ParamValue;
import cn.wis.account.model.table.Parameter;
import cn.wis.account.model.table.Provider;
import cn.wis.account.service.ParameterService;
import cn.wis.account.support.ProviderSup;

@Service
public class ParameterServiceImpl implements ParameterService {

	@Resource
	private ParameterMapper parameterMapper;

	@Resource
	private ParamValueMapper paramValueMapper;

	@Resource
	private ProviderSup providerSup;

	@Override
	public Map<String, String> confirm(String providerId) {
		Provider provider = providerSup.checkProviderId(providerId);
		List<Parameter> parameters = parameterMapper.selectAllInApp(provider.getAppId());
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

}
