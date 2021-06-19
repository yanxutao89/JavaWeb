package com.user.controller;

import com.user.service.UserReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/urc")
public class UserReadController {

    private UserReadService userReadService;
    @Autowired
    public void setUserReadService(UserReadService userReadService) {
        this.userReadService = userReadService;
    }

    @RequestMapping(value = "/retrieve/user/list", method = RequestMethod.GET)
    public Object getUserList() {
        return userReadService.getUserList("");
    }

}
