package com.ming.k8s.service.impl;

import com.ming.k8s.BaseTest;
import com.ming.k8s.service.TestPlanService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestPlanServiceImplTest extends BaseTest {
    @Autowired
    private TestPlanService testPlanService;

    @Test
    public void load() throws Exception {
        String name = testPlanService.load("JMeterTestPlan.jmx");
        //FileUtils.
        System.out.println(name);
    }
}