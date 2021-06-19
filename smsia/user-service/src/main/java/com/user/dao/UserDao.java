package com.user.dao;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2021/6/19 17:18
 */
public interface UserDao {

    List<Map> selectUserList(Map map);

}
