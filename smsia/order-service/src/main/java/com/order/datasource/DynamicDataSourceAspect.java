package com.order.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/30 21:22
 */
@Aspect
@Order(1)
@Component
public class DynamicDataSourceAspect {
	private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

	@Pointcut("execution(* com.order.dao.*.*(*))")
	private void targetDataSource() {

	}

	@Before(value = "targetDataSource()")
	public void changeDataSource(JoinPoint point) throws Exception {
		TargetDataSource dataSource = getDataSource(point);
		if (null == dataSource) {
			DynamicDataSourceContextHolder.setDataSourceType("master");
		} else {
			DynamicDataSourceContextHolder.setDataSourceType(dataSource.value());
		}
	}

	@After(value = "targetDataSource()")
	public void restoreDataSource(JoinPoint point) throws Exception {
		TargetDataSource dataSource = getDataSource(point);
		if (null == dataSource) {
			logger.debug("Restore datasource {}", "master");
		} else {
			logger.debug("Restore datasource {}", dataSource.value());
		}
		DynamicDataSourceContextHolder.clearDataSourceType();
	}

	private TargetDataSource getDataSource(JoinPoint point) throws Exception {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		Class<?> declaringClass = method.getDeclaringClass();
		TargetDataSource dataSource = declaringClass.getAnnotation(TargetDataSource.class);
		if (null != dataSource) {
			return dataSource;
		}
		return method.getAnnotation(TargetDataSource.class);
	}
}


