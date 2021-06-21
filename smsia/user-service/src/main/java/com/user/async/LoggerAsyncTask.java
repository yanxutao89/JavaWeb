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
import yanson.json.JsonObject;
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

	@Autowired
	private LoggerDao loggerDao;

	public void validateRequestNo(JoinPoint joinPoint) throws Exception {

		JsonObject jsonObject = getJsonObject(joinPoint);
		if (jsonObject.containsKey("requestNo")) {
//			Long requestNo = (Long) jsonObject.get("requestNo"); todo string -> long
			Long requestNo = Long.parseLong(jsonObject.get("requestNo").toString());
			Assert.isTrue(null == loggerDao.hasRequestNo(requestNo), String.format("Parameter 'requestNo' of %s has existed", requestNo));
		} else {
			throw new Exception("Parameter 'requestNo' is required to proceed");
		}

	}

	@Async("asyncExecutor")
	public void recordAtRequest(JoinPoint joinPoint) {

		JsonObject jsonObject = getJsonObject(joinPoint);

		LoggerPojo loggerPojo = new LoggerPojo();
		loggerPojo.setLoggerId(Util.nextId());
//		loggerPojo.setRequestNo((Long) jsonObject.get("requestNo")); todo string -> long
		loggerPojo.setRequestNo(Long.parseLong(jsonObject.get("requestNo").toString()));
		loggerPojo.setRequestMd5(Util.getMd5(jsonObject.toString()));
		loggerPojo.setRequestTime(new Date());
		Assert.isTrue(1 == loggerDao.insertLogger(loggerPojo), "Failed to insert logger");
	}

	@Async("asyncExecutor")
	public void recordAtResponse(JoinPoint joinPoint, Result result){

		JsonObject jsonObject = getJsonObject(joinPoint);
//		Long requestNo = (Long) jsonObject.get("requestNo"); todo string -> long
		Long requestNo = Long.parseLong(jsonObject.get("requestNo").toString());

		if (null != requestNo) {
			Map queryMap = new HashMap<>();
			queryMap.put("requestNo", requestNo);
			List<LoggerPojo> loggerPojos = loggerDao.selectLogger(queryMap);

			if (!CollectionUtils.isEmpty(loggerPojos)) {
				LoggerPojo loggerPojo = loggerPojos.get(0);
				loggerPojo.setResponseTime(new Date());
				loggerPojo.setResponseParams(result.toString());
				loggerPojo.setRtt(loggerPojo.getResponseTime().getTime() - loggerPojo.getRequestTime().getTime());
				Assert.isTrue(1 == loggerDao.updateLogger(loggerPojo), "Failed to update logger");
			}
		}
	}

	private JsonObject getJsonObject(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		if (!CollectionUtils.isEmpty(args)) {
			return Json.parseObject((String) args[0]);
		}
		return new JsonObject();
	}

}
