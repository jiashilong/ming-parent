package com.ming.boot.https.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/system")
public class SystemController {

    @GetMapping("/now")
    public String now() {
        LocalDateTime now = LocalDateTime.now();
        return now.toString();
    }

}
