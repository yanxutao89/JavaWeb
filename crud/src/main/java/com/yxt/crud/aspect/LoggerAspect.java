package com.yxt.crud.aspect;

import com.yxt.crud.async.LoggerAsyncTask;
import com.yxt.crud.bean.Result;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/06 13:46
 */
@Aspect
@Component
public class LoggerAspect {

	private static final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

	@Autowired
	private LoggerAsyncTask loggerAsyncTask;

	@Pointcut("@annotation(com.yxt.crud.annotations.Logger)")
	private void loggerPointcut(){

	}

	@Around(value = "loggerPointcut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		logger.info("around");
		try {
			loggerAsyncTask.validateRequestNo(joinPoint);
			loggerAsyncTask.recordAtRequest(joinPoint);
			return joinPoint.proceed();
		} catch (Exception e) {
			return exception(e);
		}
	}

	@AfterReturning(value = "loggerPointcut()", returning = "result")
	public void afterReturning(JoinPoint joinPoint, Result result){
		logger.info("afterReturning");
		loggerAsyncTask.recordAtResponse(joinPoint, result);
	}

	@AfterThrowing(value = "loggerPointcut()", argNames = "joinPoint, exception", throwing = "exception")
	public void afterThrowing(JoinPoint joinPoint, Exception exception){
		logger.info("afterThrowing");
		Result result = new Result();
		result.setMsg(exception.toString());
		loggerAsyncTask.recordAtResponse(joinPoint, result);
	}

	private Result exception(Exception e) {
		Result result = new Result();
		result.setMsg(e.getMessage());
		e.printStackTrace();
		return result;
	}

}
