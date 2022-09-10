package com.ming.jmeter.plugin;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddTest extends AbstractJavaSamplerClient {
    private static final Logger logger = LoggerFactory.getLogger(AddTest.class);
    private static final String ADD_PARAM_1 = "1";
    private static final String ADD_PARAM_2 = "2";

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result = new SampleResult();
        int a = Integer.parseInt(context.getParameter("a", ADD_PARAM_1));
        int b = Integer.parseInt(context.getParameter("b", ADD_PARAM_2));
        result.setSuccessful(true);
        result.setResponseCode("200");
        result.setResponseCodeOK();
        result.setRequestHeaders("h1=v1");
        result.setResponseMessage("ok");
        result.setContentType("application/json");
        String data = String.format("a + b = %s", a + b);
        result.setResponseData(data, "UTF-8");
        result.setDataType(SampleResult.TEXT);
        return result;
    }

    @Override
    public void setupTest(JavaSamplerContext context) {
        logger.info("AddTest setup..." + getClass().getName());
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        logger.info("AddTest teardown..." + getClass().getName());
    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument("a", ADD_PARAM_1);
        arguments.addArgument("b", ADD_PARAM_2);
        return arguments;
    }

    @Override
    protected Logger getNewLogger() {
        return logger;
    }
}
