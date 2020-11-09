package com.yxt.crud.service.impl;

import com.yxt.crud.bean.UserPojo;
import com.yxt.crud.yanson.JsonObject;
import com.yxt.crud.mapper.IUserMapper;
import com.yxt.crud.service.IUserService;
import com.yxt.crud.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/05 15:51
 */
@Service("userService")
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserMapper userMapper;

	@Override
	public Object queryUser(String jsonStr) throws Exception {

		JsonObject jsonObject = (JsonObject) JsonObject.parseObject(jsonStr);

		Map queryMap = jsonObject;

		return userMapper.selectUsers(queryMap);

	}

	@Override
	public Object addUser(String jsonStr) throws Exception {

		JsonObject jsonObject = (JsonObject) JsonObject.parseObject(jsonStr);

		UserPojo userPojo = jsonObject.toJavaObject(jsonStr, UserPojo.class);
		userPojo.setUserId(Utils.nextId());
		userPojo.setCreateTime(new Date());

		return userMapper.insertUser(userPojo);

	}

	@Override
	public Object modifyUser(String jsonStr) throws Exception {


		JsonObject jsonObject = (JsonObject) JsonObject.parseObject(jsonStr);

		UserPojo userPojo = jsonObject.toJavaObject(jsonStr, UserPojo.class);
		userPojo.setUpdateTime(new Date());

		return userMapper.updateUser(userPojo);

	}

	@Override
	public Object deleteUser(String jsonStr) throws Exception {


		JsonObject jsonObject = (JsonObject) JsonObject.parseObject(jsonStr);

		UserPojo userPojo = jsonObject.toJavaObject(jsonStr, UserPojo.class);
		userPojo.setStatus(0);
		userPojo.setUpdateTime(new Date());

		return userMapper.updateUser(userPojo);

	}
}
