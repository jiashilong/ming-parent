package com.ming.jmeter.convert;

import org.apache.jmeter.JMeter;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.SearchByClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class PlanConverter {
    private static final Logger logger = LoggerFactory.getLogger(PlanConverter.class);
    private static final String THREAD_GROUP_NUM_THREAD = "ThreadGroup.num_threads";
    private static final String THREAD_GROUP_SCHEDULER = "ThreadGroup.scheduler";
    private static final String THREAD_GROUP_DURATION = "ThreadGroup.duration";
    private static final String THREAD_GROUP_MAIN_CONTROLLER = "ThreadGroup.main_controller";
    private static final String LOOP_CONTROLLER_CONTINUE_FOREVER = "LoopController.continue_forever";
    private static final String LOOP_CONTROLLER_LOOPS = "LoopController.loops";

    private int threadNum;
    private int loops;
    private int duration;
    private String report;

    public PlanConverter() {
    }

    public HashTree convert(HashTree tree, boolean report) {
        JMeter.convertSubTree(tree, false);
        SearchByClass<ThreadGroup> threadGroupSearch = new SearchByClass<>(ThreadGroup.class);
        tree.traverse(threadGroupSearch);
        Collection<ThreadGroup> threadGroupCollection = threadGroupSearch.getSearchResults();
        for (ThreadGroup threadGroup : threadGroupCollection) {
            String name = threadGroup.getName();
            int threadNum = threadGroup.getPropertyAsInt(THREAD_GROUP_NUM_THREAD);
            boolean scheduler = threadGroup.getPropertyAsBoolean(THREAD_GROUP_SCHEDULER);
            int duration = threadGroup.getPropertyAsInt(THREAD_GROUP_DURATION);
            JMeterProperty controller = threadGroup.getProperty(THREAD_GROUP_MAIN_CONTROLLER);
            LoopController loopController = (LoopController) controller.getObjectValue();
            boolean loopForEver = loopController.getPropertyAsBoolean(LOOP_CONTROLLER_CONTINUE_FOREVER);
            int loops = loopController.getPropertyAsInt(LOOP_CONTROLLER_LOOPS);
            logger.info("ThreadGroup: name={}, threadNum={}, scheduler={}, duration={}, loops={}", name, threadNum, scheduler, duration, loops);

            // reset
            threadGroup.setProperty(THREAD_GROUP_NUM_THREAD, this.getThreadNum());
            threadGroup.setProperty(THREAD_GROUP_SCHEDULER, true);
            threadGroup.setProperty(THREAD_GROUP_DURATION, this.getDuration());
            loopController.setProperty(LOOP_CONTROLLER_CONTINUE_FOREVER, false);
            loopController.setProperty(LOOP_CONTROLLER_LOOPS, this.getLoops());
            logger.info("New ThreadGroup: name={}, threadNum={}, duration={}, loops={}", name, this.getThreadNum(), this.getDuration(), this.getLoops());
        }

        if(report) {
            this.addReport(tree);
        }
        return tree;
    }

    private void addReport(HashTree tree) {
        Summariser summer = null;
        String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
        if (summariserName.length() > 0) {
            summer = new Summariser(summariserName);
        }

        ResultCollector logger = new ResultCollector(summer);
        logger.setFilename(this.getReport());
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public int getLoops() {
        return loops;
    }

    public void setLoops(int loops) {
        this.loops = loops;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
