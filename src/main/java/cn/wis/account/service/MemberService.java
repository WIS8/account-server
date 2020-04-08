package cn.wis.account.service;

import cn.wis.account.base.BaseService;
import cn.wis.account.model.entity.Page;
import cn.wis.account.model.entity.Token;
import cn.wis.account.model.request.LoginRequest;
import cn.wis.account.model.request.PasswordUpdateRequest;
import cn.wis.account.model.request.RegisterRequest;
import cn.wis.account.model.request.RoleModifyRequest;
import cn.wis.account.model.vo.MemberVo;
import cn.wis.account.model.vo.PageVo;

public interface MemberService extends BaseService {

	void register(RegisterRequest request);

	Token login(LoginRequest request);

	MemberVo check();

	boolean logout();

	void modifyPassword(PasswordUpdateRequest request);

	void modifyRole(RoleModifyRequest request);

	PageVo search(Page page);

	MemberVo view(String memberId);

	Integer getOnlineInfo();

}
