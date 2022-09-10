package com.ming.jmeter.plugin;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestStateListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;

public class PingSampler extends AbstractSampler implements TestStateListener {
    private static final String PING_TARGET = "ping.target";
    private static final String DEFAULT_ENCODE = "UTF-8";

    public PingSampler() {
        setName("PingSampler");
    }

    @Override
    public SampleResult sample(Entry entry) {
        SampleResult result = new SampleResult();
        try {
            result.setSampleLabel(getName());
            result.sampleStart();
            /**
             * 采样器执行code处
             * **/
            String target = this.getTarget();
            boolean pingable = this.ping(target, 3000);
            result.sampleEnd();
            result.setSamplerData(String.valueOf(pingable));
            result.setSuccessful(true);
            result.setResponseMessage(String.valueOf(pingable));
            result.setDataType(SampleResult.TEXT);
            result.setResponseCode("OK");
            result.setResponseData(String.valueOf(pingable), DEFAULT_ENCODE);
        } catch (Exception e) {
            result.sampleEnd();
            result.setSuccessful(false);
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            String message = stringWriter.toString();
            result.setResponseMessage(message);
            result.setResponseData(message, DEFAULT_ENCODE);
            result.setDataType(SampleResult.TEXT);
            result.setResponseCode("FAILED");
        }
        return result;
    }

    @Override
    public void testStarted() {

    }

    @Override
    public void testStarted(String host) {

    }

    @Override
    public void testEnded() {

    }

    @Override
    public void testEnded(String host) {

    }
    public void setTarget(String target){
        setProperty(PING_TARGET, target);
    }

    public String getTarget(){
        return getPropertyAsString(PING_TARGET);
    }

    public boolean ping(String target, int timeout) throws IOException {
        boolean status = InetAddress.getByName(target).isReachable(timeout);
        return status;
    }
}
