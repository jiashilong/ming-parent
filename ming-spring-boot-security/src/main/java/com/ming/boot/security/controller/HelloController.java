package com.ming.boot.security.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shilong.jia
 * @version 1.0.0
 * @createTime 2021-11-24
 * @Description
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping(value = "/greeting/{username}", method = RequestMethod.GET)
    public String greeting(@PathVariable String username) {
        return "hello " + username;
    }
}

