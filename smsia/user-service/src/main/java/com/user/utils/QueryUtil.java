package com.user.utils;


import com.user.async.QueryTask;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/09/03 10:21
 */
@Component
public class QueryUtil {

	public static Map<String, Object> getResultMap(QueryTask... queryTasks) throws InterruptedException, ExecutionException, TimeoutException {

		int taskCount = queryTasks.length;
		Map<String, Object> map = new HashMap(taskCount);
		Future[] futures = new Future[taskCount];

		for (int i = 0; i < taskCount; ++i) {
			Future future = queryTasks[i].getResult();
			futures[i] = future;
		}

		for (int i = 0; i < taskCount; ++i) {
			Object r = futures[i].get(30, TimeUnit.SECONDS);
			map.put(queryTasks[i].getMethodName(), r);
		}

		return map;
	}

}
