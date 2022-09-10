package com.ming.boot.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shilong.jia
 * @version 1.0.0
 * @createTime 2021-12-22
 * @Description
 */
@RestController
public class HealthController {
    private static String HEALTH = "health";

    @RequestMapping("/health")
    public String health() {
        return HEALTH;
    }
}
