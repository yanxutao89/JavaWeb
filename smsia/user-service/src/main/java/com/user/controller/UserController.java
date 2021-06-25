package com.user.controller;

import com.user.annotations.MsLog;
import com.user.model.Result;
import com.user.service.UserService;
import com.user.utils.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {
    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @MsLog("获取用户列表")
    @RequestMapping(value = "/retrieve/list", method = RequestMethod.GET)
    public Result getUserList(@RequestBody String str) {
        return userService.getUserList(str);
    }

    @MsLog("新增用户")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result createUser(@RequestBody String str) {
        return userService.createUser(str);
    }
}
