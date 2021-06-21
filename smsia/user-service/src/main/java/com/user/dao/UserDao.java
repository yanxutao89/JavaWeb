package com.user.dao;

import com.user.model.UserPojo;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2021/6/19 17:18
 */
public interface UserDao {

    List<Map> selectUserList(Map map);

    Integer insertUser(UserPojo record);

}
