package cn.wis.account.service;

import java.util.Map;

import cn.wis.account.base.BaseService;

public interface ParameterService extends BaseService {

	Map<String, String> confirm(String providerId);

}
