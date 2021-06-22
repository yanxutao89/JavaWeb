package com.user.async;


import com.user.dao.LoggerDao;
import com.user.model.LoggerPojo;
import com.user.model.Result;
import com.user.utils.Util;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import yanson.json.Json;
import yanson.utils.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/06 16:08
 */
@Component
public class LoggerAsyncTask {
	private LoggerDao loggerDao;
	@Autowired
	public void setLoggerDao(LoggerDao loggerDao) {
		this.loggerDao = loggerDao;
	}

	public void validateRequestNo(JoinPoint joinPoint) throws Exception {
		LoggerPojo loggerPojo = getLoggerPojo(joinPoint);
		if (null != loggerPojo.getRequestNo()) {
			Long requestNo = loggerPojo.getRequestNo();
			Assert.isTrue(null == loggerDao.hasRequestNo(requestNo), String.format("Parameter 'requestNo' of %s has existed", requestNo));
		}
		else {
			throw new Exception("Parameter 'requestNo' is required to proceed");
		}
	}

	@Async("asyncExecutor")
	public void recordAtRequest(JoinPoint joinPoint) {
		LoggerPojo loggerPojo = getLoggerPojo(joinPoint);
		loggerPojo.setLoggerId(Util.nextId());
		loggerPojo.setRequestTime(new Date());
		loggerPojo.setRequestMd5(Util.getMd5(loggerPojo.toString()));
		Assert.isTrue(1 == loggerDao.insertLogger(loggerPojo), "Failed to insert logger");
	}

	@Async("asyncExecutor")
	public void recordAtResponse(JoinPoint joinPoint, Result result){
		LoggerPojo loggerPojo = getLoggerPojo(joinPoint);
		Long requestNo = loggerPojo.getRequestNo();

		if (null != requestNo) {
			Map queryMap = new HashMap<>();
			queryMap.put("requestNo", requestNo);
			List<LoggerPojo> loggerPojos = loggerDao.selectLogger(queryMap);

			if (!CollectionUtils.isEmpty(loggerPojos)) {
				LoggerPojo logger = loggerPojos.get(0);
				logger.setResponseTime(new Date());
				logger.setResponseParams(result.toString());
				logger.setRtt(logger.getResponseTime().getTime() - logger.getRequestTime().getTime());
				Assert.isTrue(1 == loggerDao.updateLogger(logger), "Failed to update logger");
			}
		}
	}

	private LoggerPojo getLoggerPojo(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		if (!CollectionUtils.isEmpty(args)) {
			return Json.parseObject((String) args[0]).toJavaObject(LoggerPojo.class);
		}
		return new LoggerPojo();
	}
}
