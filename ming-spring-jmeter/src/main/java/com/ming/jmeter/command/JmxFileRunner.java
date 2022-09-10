package com.ming.jmeter.command;

import com.ming.jmeter.convert.PlanConverter;
import com.ming.jmeter.executor.PlanExecutor;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

//@Component
public class JmxFileRunner implements CommandLineRunner, PlanExecutor {
    private static final Logger logger = LoggerFactory.getLogger(JmxFileRunner.class);

    @PostConstruct
    public void init() throws IOException {
        JMeterUtils.setJMeterHome(this.getJMeterHomePath());
        JMeterUtils.loadJMeterProperties(this.getJMeterPropertyPath());
        JMeterUtils.initLocale();
        SaveService.loadProperties();
    }

    private HashTree format(HashTree tree) {
        PlanConverter converter = new PlanConverter();
        converter.setThreadNum(this.getThreadNum());
        converter.setDuration(this.getDuration());
        converter.setLoops(this.getLoops());
        converter.setReport(this.getReport());
        tree = converter.convert(tree, true);
        return tree;
    }

    @Override
    public void run(String... args) throws Exception {
        StandardJMeterEngine jMeterEngine = new StandardJMeterEngine();
        File jmxFile = new File(this.getJmxPath());

        HashTree planTree = SaveService.loadTree(jmxFile);
        planTree = this.format(planTree);

        long start = System.currentTimeMillis();
        jMeterEngine.configure(planTree);
        jMeterEngine.run();
        long end = System.currentTimeMillis();
        logger.info("TestPlan Execute Finished..., Time take: {}", end - start);
    }

    @Override
    public int getThreadNum() {
        return 50;
    }

    @Override
    public int getDuration() {
        return 10;
    }

    @Override
    public String getName() {
        return "测试计划01";
    }

    public String getJmxPath() {
        return JMeterUtils.getJMeterBinDir() + "/ExtendTest.jmx";
    }

}
