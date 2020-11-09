package com.yxt.crud.aspect;

import com.yxt.crud.annotations.Crud;
import com.yxt.crud.async.LoggerAsyncTask;
import com.yxt.crud.bean.Result;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/06 13:46
 */
@Aspect
@Component
public class CrudAspect {

	private static final Logger logger = LoggerFactory.getLogger(CrudAspect.class);

	@Autowired
	private LoggerAsyncTask loggerAsyncTask;

	@Pointcut("@annotation(com.yxt.crud.annotations.Crud)")
	private void loggerPointcut(){

	}

	@Around(value = "loggerPointcut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		logger.info("around");
		try {
			loggerAsyncTask.validateRequestNo(joinPoint);
			loggerAsyncTask.recordAtRequest(joinPoint);
			Object data = joinPoint.proceed();
			return normal(data, joinPoint);
		} catch (Exception e) {
			return exception(e, joinPoint);
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

	private Result normal(Object data, JoinPoint joinPoint) throws NoSuchMethodException {
		Result result;
		if (data instanceof Result) {
			result = (Result) data;
		} else {
			result = new Result();
		}
		result.setCode(null == result.getCode() ? 200 : result.getCode());
		Crud crud = getAnnotation(joinPoint);
		result.setMsg(null == result.getMsg() ? null == crud ? "操作成功" : crud.value() + "成功" : result.getMsg());
		result.setData(null == result.getData() ? data : result.getData());
		return result;
	}

	private Result exception(Exception e, JoinPoint joinPoint) throws NoSuchMethodException {
		Result result = new Result();
		Crud crud = getAnnotation(joinPoint);
		result.setMsg(null == crud ? "操作失败" : crud.value() + "失败" + e.getMessage());
		e.printStackTrace();
		return result;
	}

	private Crud getAnnotation(JoinPoint joinPoint) throws NoSuchMethodException {
		Class<?> clazz = joinPoint.getTarget().getClass();
		Object[] args = joinPoint.getArgs();
		Class[] parameterTypes = new Class[args.length];
		for (int i = 0; i < args.length; ++i) {
			parameterTypes[i] = args[i].getClass();
		}
		String name = joinPoint.getSignature().getName();
		Method method = clazz.getDeclaredMethod(name, parameterTypes);
		return method.getAnnotation(Crud.class);
	}

}
