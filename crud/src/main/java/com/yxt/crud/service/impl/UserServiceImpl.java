package com.yxt.crud.service.impl;

import com.yxt.crud.bean.Result;
import com.yxt.crud.bean.UserPojo;
import com.yxt.crud.json.JsonObject;
import com.yxt.crud.mapper.IUserMapper;
import com.yxt.crud.service.IUserService;
import com.yxt.crud.utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
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
	public Result queryUser(String jsonStr) throws Exception {

		Result result = new Result();

		JsonObject jsonObject = (JsonObject) JsonObject.parseObject(jsonStr);
		Map queryMap = jsonObject.toJavaObject(jsonStr, Map.class);

		result.setCode(200);
		result.setMsg("获取成功");
		result.setData(userMapper.selectUsers(queryMap));

		return result;

	}

	@Override
	public Result addUser(String jsonStr) throws Exception {

		Result result = new Result();

		JsonObject jsonObject = (JsonObject) JsonObject.parseObject(jsonStr);
		UserPojo userPojo = jsonObject.toJavaObject(jsonStr, UserPojo.class);
		userPojo.setUserId(IdUtils.nextId());
		userPojo.setCreateTime(new Date());

		result.setCode(200);
		result.setMsg("操作成功");
		result.setData(userMapper.insertUser(userPojo));

		return result;

	}

	@Override
	public Result modifyUser(String jsonStr) throws Exception {

		Result result = new Result();

		JsonObject jsonObject = (JsonObject) JsonObject.parseObject(jsonStr);
		UserPojo userPojo = jsonObject.toJavaObject(jsonStr, UserPojo.class);
		userPojo.setUpdateTime(new Date());

		result.setCode(200);
		result.setMsg("操作成功");
		result.setData(userMapper.updateUser(userPojo));

		return result;

	}

	@Override
	public Result deleteUser(String jsonStr) throws Exception {

		Result result = new Result();

		JsonObject jsonObject = (JsonObject) JsonObject.parseObject(jsonStr);
		UserPojo userPojo = jsonObject.toJavaObject(jsonStr, UserPojo.class);
		userPojo.setStatus(0);
		userPojo.setUpdateTime(new Date());

		result.setCode(200);
		result.setMsg("操作成功");
		result.setData(userMapper.updateUser(userPojo));

		return result;

	}

}
