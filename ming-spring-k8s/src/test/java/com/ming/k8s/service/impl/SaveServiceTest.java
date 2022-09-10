package com.ming.k8s.service.impl;

import com.ming.k8s.BaseTest;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.ListedHashTree;
import org.junit.Test;

import java.io.File;
import java.util.Collection;
import java.util.Map;

public class SaveServiceTest extends BaseTest {

    @Test
    public void test() throws Exception {
        JMeterUtils.setJMeterHome("D:\\Local\\apache-jmeter-5.4.3");
        ListedHashTree hashTree = (ListedHashTree) SaveService.loadTree(new File("JMeterTestPlan.jmx"));
        for (Object o : hashTree.getArray()) {
            HashTree tree = hashTree.get(o);
        }
    }
}
