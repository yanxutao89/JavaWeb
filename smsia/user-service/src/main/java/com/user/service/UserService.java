package com.user.service;

import com.user.model.Result;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2021/6/19 17:22
 */
public interface UserService {
    Result getUserList(String str);

    Result createUser(String str);
}
