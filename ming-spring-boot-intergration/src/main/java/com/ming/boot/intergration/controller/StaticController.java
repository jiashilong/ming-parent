package com.ming.boot.intergration.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/static")
public class StaticController {

    // @GetMapping("/p2.png")
    public String p2() {
        return "this is p2 page...";
    }
}
