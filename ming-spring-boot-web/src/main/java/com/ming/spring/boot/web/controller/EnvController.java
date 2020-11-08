package com.ming.spring.boot.web.controller;

import com.ming.common.model.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/env")
public class EnvController {
    private static final String HEALTH_OK = "ok";

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public Result<String> health() {
        return Result.success(HEALTH_OK);
    }
}
