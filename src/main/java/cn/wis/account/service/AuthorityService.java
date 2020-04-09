package cn.wis.account.service;

import java.util.List;

import cn.wis.account.base.BaseService;
import cn.wis.account.model.request.AuthAddRequest;
import cn.wis.account.model.request.AuthDeleteRequest;
import cn.wis.account.model.vo.AuthVo;

public interface AuthorityService extends BaseService {

	String add(AuthAddRequest request);

	void remove(AuthDeleteRequest request);

	List<AuthVo> search(String apiId);

}
