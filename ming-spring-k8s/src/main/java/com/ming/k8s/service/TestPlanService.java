package com.ming.k8s.service;

import org.dom4j.DocumentException;

import java.io.IOException;

public interface TestPlanService {
    String load(String xmlFile) throws DocumentException, IOException;
}
