package com.user.service.impl;

import com.user.dao.UserDao;
import com.user.model.Result;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
        List<Map> userList = userDao.selectUserList(new HashMap());
        return result.setCode(200).setMsg("获取成功").setData(userList);
    }
}
