package com.ming.boot.web.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hi")
public class HiController {

    @RequestMapping(value = "/greet", method = RequestMethod.GET)
    public String greeting(String name, Integer age) {
        String msg = String.format("hi %s, your age is %d", name, age);
        return msg;
    }

    @RequestMapping(value = "/info/{name}/en/{age}", method = RequestMethod.GET)
    public String info(@PathVariable String name, @PathVariable Integer age) {
        String msg = String.format("hi en: %s, your age is %d", name, age);
        return msg;
    }
}
