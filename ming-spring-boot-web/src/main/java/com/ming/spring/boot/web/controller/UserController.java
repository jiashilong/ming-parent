package com.ming.spring.boot.web.controller;

import com.ming.common.model.Result;
import com.ming.common.model.User;
import com.ming.spring.boot.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getUserById", method = RequestMethod.GET)
    public Result<User> getUserById(@RequestParam(name = "id", required = true, defaultValue = "0") int id) {
        int a = 1 / 0;
        User user = userService.getUser(id);
        return Result.success(user);
    }

    @RequestMapping(value = "getDefaultUser", method = RequestMethod.GET)
    public User getDefaultUser() {
        return userService.getUser(0);
    }
}
