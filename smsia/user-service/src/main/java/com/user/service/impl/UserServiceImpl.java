package com.user.service.impl;

import com.user.dao.UserDao;
import com.user.model.Result;
import com.user.model.UserPojo;
import com.user.service.UserService;
import com.user.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import yanson.json.Json;
import yanson.json.JsonObject;

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

    @Override
    public Result getUserList(String str) {
        Result result = new Result();
        JsonObject getMap = Json.parseObject(str);
        List<Map> userList = userDao.selectUserList(getMap);
        return result.setCode(200).setMsg("获取成功").setData(userList);
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
