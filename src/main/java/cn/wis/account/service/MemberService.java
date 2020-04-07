package cn.wis.account.service;

import cn.wis.account.base.BaseService;
import cn.wis.account.model.entity.Token;
import cn.wis.account.model.request.LoginRequest;
import cn.wis.account.model.request.PasswordUpdateRequest;
import cn.wis.account.model.request.RegisterRequest;
import cn.wis.account.model.vo.MemberVo;

public interface MemberService extends BaseService {

	void register(RegisterRequest request);

	Token login(LoginRequest request);

	MemberVo check();

	boolean logout();

	void modifyPassword(PasswordUpdateRequest request);

}
