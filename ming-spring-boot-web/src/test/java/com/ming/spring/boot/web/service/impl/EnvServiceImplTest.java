package com.ming.spring.boot.web.service.impl;

import com.ming.spring.boot.common.test.BaseTest;
import com.ming.spring.boot.web.service.EnvService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class EnvServiceImplTest extends BaseTest {
    @Autowired
    private EnvService envService;

    @Test
    public void getEnvironment() {
        System.out.println(envService.getEnvironment());
    }

    @Test
    public void isDaily() {
        System.out.println(envService.isDaily());
    }
}