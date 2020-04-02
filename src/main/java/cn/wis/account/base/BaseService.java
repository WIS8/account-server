package cn.wis.account.base;

import javax.servlet.http.HttpServletRequest;

import cn.wis.account.config.Constant;
import cn.wis.account.model.vo.MemberVo;

public interface BaseService {

	default MemberVo getLoginMember(HttpServletRequest request) {
		return (MemberVo) request.getAttribute(Constant.MEMBER_INFO);
	}

}
