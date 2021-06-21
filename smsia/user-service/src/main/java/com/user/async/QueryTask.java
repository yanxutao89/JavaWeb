package com.user.async;


import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/09/03 9:48
 */
public class QueryTask<R> {

	private Object dao;
	private String methodName;
	private Class<?>[] paramTypes;
	private Object[] args;

	private QueryTask(Object dao, String methodName, Class<?>[] paramTypes, Object[] args) throws Exception {
		if (null == dao) {
			throw new Exception("Parameter 'dao' must not be null");
		}
		this.dao = dao;
		if (null == methodName) {
			throw new Exception("Parameter 'methodName' must not be null");
		}
		this.methodName = methodName;
		this.paramTypes = paramTypes;
		this.args = args;
	}

	public Object getDao() {
		return dao;
	}

	public String getMethodName() {
		return methodName;
	}

	public Class<?>[] getParamTypes() {
		return paramTypes;
	}

	public Object[] getArgs() {
		return args;
	}

	@Async("asyncExecutor")
	public Future<R> getResult() {
		R r = null;
		try {
			Method method = dao.getClass().getDeclaredMethod(methodName, paramTypes);
			r =  (R) method.invoke(dao, getArgs());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new AsyncResult<>(r);
	}

	public static final class Builder {

		private Object dao;
		private String methodName;
		private Class<?>[] paramTypes;
		private Object[] args;

		public Builder dao(Object dao) {
			this.dao = dao;
			return this;
		}

		public Builder methodName(String methodName) {
			this.methodName = methodName;
			return this;
		}

		public Builder paramTypes(Class<?>... paramTypes) {
			this.paramTypes = paramTypes;
			return this;
		}

		public Builder args(Object... args) {
			this.args = args;
			return this;
		}

		public QueryTask build() throws Exception {
			return new QueryTask(dao, methodName, paramTypes, args);
		}
	}

}
