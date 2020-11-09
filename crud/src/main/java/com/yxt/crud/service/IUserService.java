package com.yxt.crud.service;

import com.yxt.crud.bean.Result;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/05 15:50
 */
public interface IUserService {

	Object queryUser(String jsonStr) throws Exception;

	Object addUser(String jsonStr) throws Exception;

	Object modifyUser(String jsonStr) throws Exception;

	Object deleteUser(String jsonStr) throws Exception;

}
