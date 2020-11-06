package com.yxt.crud.service;

import com.yxt.crud.bean.Result;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/05 15:50
 */
public interface IUserService {

	Result queryUser(String jsonStr) throws Exception;

	Result addUser(String jsonStr) throws Exception;

	Result modifyUser(String jsonStr) throws Exception;

	Result deleteUser(String jsonStr) throws Exception;

}
