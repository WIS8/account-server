package cn.wis.account.service;

import cn.wis.account.base.BaseService;
import cn.wis.account.model.entity.Page;
import cn.wis.account.model.request.RoleAddRequest;
import cn.wis.account.model.request.RoleUpdateRequest;
import cn.wis.account.model.vo.PageVo;

public interface RoleService extends BaseService {

	void add(RoleAddRequest request);

	void delete(String roleId);

	void update(RoleUpdateRequest request);

	PageVo search(Page page);

}
