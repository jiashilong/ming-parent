package com.ming.k8s.service.impl;

import com.ming.k8s.service.TestPlanService;
import org.apache.jmeter.save.SaveService;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TestPlanServiceImpl implements TestPlanService {
    @Override
    public String load(String xmlFile) throws DocumentException, IOException {
        SaveService saveService = new SaveService();
        SaveService.loadTree(null);
        return null;
    }



}
