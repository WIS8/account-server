package cn.wis.account.aop;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import cn.wis.account.component.TokenManager;
import cn.wis.account.config.Constant;
import cn.wis.account.model.entity.Token;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;

@Aspect
@Order(10)
public class TokenAspect {

	private final static Set<String> NO_TOKEN_METHOD;

	static {
		NO_TOKEN_METHOD = new HashSet<String>(Arrays
				.asList("register", "login", "confirm"));
	}

	@Resource
	private TokenManager tokenManager;

	@Resource
	private HttpServletRequest request;

	@Around("execution(public * cn.wis.account.controller.*.*(..))")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		MethodSignature signature = (MethodSignature) point.getSignature();
		if (NO_TOKEN_METHOD.contains(signature.getMethod().getName())) {
			return point.proceed();
		}
		String cookie = request.getHeader(Constant.COOKIE_NAME);
		if (cookie == null) {
			throw new ServiceException(ResultEnum.WITHOUT_COOKIE);
		}
		Token token = tokenManager.getToken(cookie);
		if (token == null) {
			throw new ServiceException(ResultEnum.INVALID_COOKIE);
		}
		if (token.getCookie() == null) {
			throw new ServiceException(ResultEnum.SERVICE);
		}
		request.setAttribute(Constant.MEMBER_INFO, token.getMemberVo());
		return point.proceed();
	}

}
