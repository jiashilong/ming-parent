package com.ming.consul.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/system")
public class SystemInfoController {
    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.cloud.client.ip-address}")
    private String ip;

    @Value("${server.port}")
    private int port;

    @GetMapping("/info")
    public Map<String, String> info() {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("application", appName);
        infoMap.put("ip", ip);
        infoMap.put("port", String.valueOf(port));
        infoMap.put("time", String.valueOf(System.currentTimeMillis()));
        return infoMap;
    }
}
