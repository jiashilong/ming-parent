package com.ming.boot.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/page")
public class PageController {

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public Map<String, Integer> query(@RequestParam int pageNum, @RequestParam int pageSize) {
        Map<String, Integer> result = new HashMap<>();
        return result;
    }
}
