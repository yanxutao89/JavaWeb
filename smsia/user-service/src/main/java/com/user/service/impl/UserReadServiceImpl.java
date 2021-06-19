package com.user.service.impl;

import com.user.dao.UserDao;
import com.user.service.UserReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2021/6/19 17:22
 */
@Service
public class UserReadServiceImpl implements UserReadService {

    private UserDao userDao;
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Object getUserList(String str) {
        return userDao.selectUserList(new HashMap());
    }
}
