package com.user.controller;

import com.user.model.Result;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/retrieve/userList", method = RequestMethod.GET)
    public Result getUserList() {
        return userService.getUserList("");
    }

}
