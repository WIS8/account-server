package cn.wis.account.aop;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.wis.account.model.result.Result;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public Result handleUnknownException(Exception e) {
		log.error(ExceptionUtil.stacktraceToString(e));
		return Result.getResult(ResultEnum.UNKNOWN, e.getMessage());
	}

	@ExceptionHandler(ServiceException.class)
	public Result handleServiceException(ServiceException e) {
		return Result.getResult(e.getResultEnum(), e.getMessage());
	}

}
