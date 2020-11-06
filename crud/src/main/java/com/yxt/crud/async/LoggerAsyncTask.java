package com.yxt.crud.async;

import com.yxt.crud.bean.LoggerPojo;
import com.yxt.crud.bean.Result;
import com.yxt.crud.json.JsonObject;
import com.yxt.crud.mapper.ILoggerMapper;
import com.yxt.crud.utils.CollectionUtils;
import com.yxt.crud.utils.Utils;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/06 16:08
 */
@Component
public class LoggerAsyncTask {

	@Autowired
	private ILoggerMapper loggerMapper;

	public void validateRequestNo(JoinPoint joinPoint) throws Exception {

		JsonObject jsonObject = getJsonObject(joinPoint);
		if (jsonObject.containsKey("requestNo")) {
			Long requestNo = (Long) jsonObject.get("requestNo");
			Assert.isTrue(null == loggerMapper.hasRequestNo(requestNo), String.format("Parameter 'requestNo' of %s has existed", requestNo));
		} else {
			throw new Exception("Parameter 'requestNo' is required to proceed");
		}

	}

	@Async("asyncExecutor")
	public void recordAtRequest(JoinPoint joinPoint) {

		JsonObject jsonObject = getJsonObject(joinPoint);

		LoggerPojo loggerPojo = new LoggerPojo();
		loggerPojo.setLoggerId(Utils.nextId());
		loggerPojo.setRequestNo((Long) jsonObject.get("requestNo"));
		loggerPojo.setRequestMd5(Utils.getMd5(jsonObject.toJsonString()));
		loggerPojo.setRequestTime(new Date());
		Assert.isTrue(1 == loggerMapper.insertLogger(loggerPojo), "Failed to insert logger");
	}

	@Async("asyncExecutor")
	public void recordAtResponse(JoinPoint joinPoint, Result result){

		JsonObject jsonObject = getJsonObject(joinPoint);
		Long requestNo = (Long) jsonObject.get("requestNo");

		if (null != requestNo) {
			Map queryMap = new HashMap<>();
			queryMap.put("requestNo", requestNo);
			List<LoggerPojo> loggerPojos = loggerMapper.selectLogger(queryMap);

			if (!CollectionUtils.isEmpty(loggerPojos)) {
				LoggerPojo loggerPojo = loggerPojos.get(0);
				loggerPojo.setResponseTime(new Date());
				loggerPojo.setResponseParams(result.toString());
				loggerPojo.setRtt(loggerPojo.getResponseTime().getTime() - loggerPojo.getRequestTime().getTime());
				Assert.isTrue(1 == loggerMapper.updateLogger(loggerPojo), "Failed to update logger");
			}
		}
	}

	private JsonObject getJsonObject(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		if (!CollectionUtils.isEmpty(args)) {
			return (JsonObject) JsonObject.parseObject((String) args[0]);
		}
		return new JsonObject();
	}

}
