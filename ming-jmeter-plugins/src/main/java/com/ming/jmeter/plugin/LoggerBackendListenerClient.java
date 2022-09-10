package com.ming.jmeter.plugin;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.backend.AbstractBackendListenerClient;
import org.apache.jmeter.visualizers.backend.BackendListenerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LoggerBackendListenerClient extends AbstractBackendListenerClient {
    private static final Logger logger = LoggerFactory.getLogger(LoggerBackendListenerClient.class);
    private static final String DEFAULT_APPLICATION_NAME = "default";

    @Override
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument("application", DEFAULT_APPLICATION_NAME);
        return arguments;
    }

    @Override
    public void setupTest(BackendListenerContext context) throws Exception {
        super.setupTest(context);
        logger.info("LoggerBackendListenerClient Init...");
    }

    @Override
    public void teardownTest(BackendListenerContext context) throws Exception {
        super.teardownTest(context);
        logger.info("LoggerBackendListenerClient Finish...");
    }

    @Override
    public void handleSampleResults(List<SampleResult> sampleResultList, BackendListenerContext context) {
        for (SampleResult sampleResult : sampleResultList) {
            String threadName = sampleResult.getThreadName();
            int threadCount = sampleResult.getAllThreads();
            String responseData = sampleResult.getResponseDataAsString();
            logger.info("LoggerBackendListenerClient: threadCount={}, threadName={}, responseData={}", threadCount, threadName, responseData);
        }
    }
}
