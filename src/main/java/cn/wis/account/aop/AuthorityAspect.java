package cn.wis.account.aop;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import cn.wis.account.component.AccountInitializer;
import cn.wis.account.component.AuthorityRule;
import cn.wis.account.config.Constant;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.model.vo.MemberVo;

@Aspect
@Order(20)
public class AuthorityAspect {

	@Resource
	private HttpServletRequest request;

	@Resource
	private AuthorityRule authorityRule;

	@Resource
	private AccountInitializer intializer;

	@Around("@annotation(cn.wis.account.aop.Rule) && @annotation(rule)")
    public Object advice(ProceedingJoinPoint point, Rule rule) throws Throwable {
		if (rule.value().isAllow(inListOf(point))) {
	        return point.proceed();
		}
		throw new ServiceException(ResultEnum.INHIBIT);
    }

	private boolean inListOf(ProceedingJoinPoint point) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Object result = request.getAttribute(Constant.MEMBER_INFO);
		String roleId = (result == null) ? Constant.VISIT_ROLE : ((MemberVo) result).getRoleId();
		return authorityRule.isBound(intializer.makeMethodKey(signature.getMethod()), roleId);
	}

}
