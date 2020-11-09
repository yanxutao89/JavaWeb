package com.yxt.crud.controller;

import com.yxt.crud.annotations.Crud;
import com.yxt.crud.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/11/05 16:29
 */
@RestController("userController")
public class UserController {

	@Autowired
	private IUserService userService;

	@Crud(value = "查询用户")
	@RequestMapping(value = "/queryUser", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	public Object queryUser(@RequestBody String jsonStr) throws Exception {
		return userService.queryUser(jsonStr);
	}

	@Crud(value = "添加用户")
	@RequestMapping(value = "/addUser", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	public Object addUser(@RequestBody String jsonStr) throws Exception {
		return userService.addUser(jsonStr);
	}

	@Crud(value = "修改用户")
	@RequestMapping(value = "/modifyUser", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	public Object modifyUser(@RequestBody String jsonStr) throws Exception {
		return userService.modifyUser(jsonStr);
	}

	@Crud(value = "删除用户")
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	public Object deleteUser(@RequestBody String jsonStr) throws Exception {
		return userService.deleteUser(jsonStr);
	}

}
