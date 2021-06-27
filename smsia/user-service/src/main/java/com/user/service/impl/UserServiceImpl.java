package com.user.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.user.client.OrderServiceClient;
import com.user.dao.UserDao;
import com.user.model.Result;
import com.user.model.UserPojo;
import com.user.service.UserService;
import com.user.utils.UserContextHolder;
import com.user.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import yanson.json.Json;
import yanson.json.JsonObject;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2021/6/19 17:22
 */
@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private OrderServiceClient orderServiceClient;

    @Autowired
    public void setOrderServiceClient(OrderServiceClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "buildFallbackUserList",
            threadPoolKey = "asyncExecutor",
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")
            })
    public Result getUserList(String str) {
        System.err.println(UserContextHolder.getContext().getCorrelationId());
        Result result = new Result();
        JsonObject getMap = Json.parseObject(str);
        List<Map> userList = userDao.selectUserList(getMap);
        Result orderList = orderServiceClient.getOrderService(str);
        Object data = orderList.getData();
        userList.addAll((List) data);
        return result.setCode(200).setMsg("获取成功").setData(userList);
    }

    private Result buildFallbackUserList(String str) {
        return new Result().setCode(200).setMsg("获取成功").setData(Collections.emptyList());
    }

    @Override
    public Result createUser(String str) {
        Result result = new Result();
        UserPojo userRecord = Json.parseObject(str).toJavaObject(UserPojo.class);
        userRecord.setUserId(Util.nextId());
        userRecord.setCreateTime(new Date());
        userRecord.setStatus(1);
        Assert.isTrue(1 == userDao.insertUser(userRecord), "Failed to insert user");
        return result.setCode(200).setMsg("操作成功").setData(userRecord);
    }
}
