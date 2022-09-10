package com.ming.jmeter.command;

import com.ming.jmeter.executor.PlanExecutor;
import org.apache.jmeter.config.Argument;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.CSVDataSet;
import org.apache.jmeter.config.CSVDataSetBeanInfo;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testbeans.gui.TestBeanGUI;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.testelement.property.*;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.timers.ConstantThroughputTimer;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.backend.BackendListener;
import org.apache.jmeter.visualizers.backend.BackendListenerGui;
import org.apache.jmeter.visualizers.backend.influxdb.InfluxdbBackendListenerClient;
import org.apache.jorphan.collections.HashTree;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JmxApiRunner implements CommandLineRunner, PlanExecutor {
    public static final String JMETER_ENCODING = "UTF-8";

    public static final int NUMBER_THREADS = 1;
    public static final String replayLogPath = "D:\\Tmp\\jmeter\\replay_result.log";
    public static final String summaryLogPath = "D:\\Tmp\\jmeter\\summary_result.log";
    public static final String jmxPath = "D:\\Tmp\\jmeter\\test.jmx";
    private static Integer runType=1;
    private static Integer runCount=10;


    @Override
    public void run(String... args) throws Exception {
        String url = "localhost";
        String port = "9090";
        String api = "/web/health";
        String query = "planId=54";
        String method = "GET";
        // JMeterUtils.setJMeterHome(this.getJMeterHomePath());
        JMeterUtils.loadJMeterProperties(this.getJMeterPropertyPath());

        String paths = JMeterUtils.getJMeterProperties().getProperty("search_paths");
        System.err.println(paths);

        // TestPlan
        TestPlan testPlan = getTestPlan();

        // 循环控制器
        LoopController loopController = getLoopController();

        // 线程组
        ThreadGroup threadGroup = getThreadGroup(loopController, NUMBER_THREADS);

        // Http请求信息
        HTTPSamplerProxy httpSamplerProxy = getHttpSamplerProxy(url, port, api, query,method);

        // 结果：如汇总报告、察看结果树
        List<ResultCollector> resultCollectorList = getResultCollector();

        // 设置吞吐量
        // ConstantThroughputTimer constantThroughputTimer = getConstantThroughputTimer(20);

        // 请求头信息
        HeaderManager headerManager = getHeaderManager();

        HashTree sampleHashTree = new HashTree();
        sampleHashTree.add(headerManager);
        // sampleHashTree.add(constantThroughputTimer);

        HashTree elementHashTree = new HashTree();
        // Sampler
        elementHashTree.add(httpSamplerProxy, sampleHashTree);

        // Listener
        for (ResultCollector resultCollector : resultCollectorList) {
            elementHashTree.add(resultCollector);
        }
        BackendListener influxListener = getInfluxBackendListener();
        BackendListener loggerListener = getLoggerBackendListener();
        elementHashTree.add(influxListener);
        elementHashTree.add(loggerListener);

        HashTree threadGroupHashTree = new HashTree();
        threadGroupHashTree.add(threadGroup, elementHashTree);

        HashTree planHashTree = new HashTree();
        planHashTree.add(testPlan, threadGroupHashTree);

        try {
            SaveService.saveTree(planHashTree, new FileOutputStream(jmxPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StandardJMeterEngine standardJMeterEngine = new StandardJMeterEngine();
        standardJMeterEngine.configure(planHashTree);
        standardJMeterEngine.run();
        System.out.println("运行成功!!!");
    }

    @Override
    public int getThreadNum() {
        return 1;
    }

    @Override
    public int getDuration() {
        return 10;
    }

    @Override
    public String getName() {
        return "Test01";
    }

    private static BackendListener getLoggerBackendListener() {
        BackendListener backendListener = new BackendListener();
        backendListener.setProperty(new StringProperty("TestElement.name", "LoggerListener"));
        backendListener.setProperty(new StringProperty("TestElement.gui_class", BackendListenerGui.class.getName()));
        backendListener.setProperty(new StringProperty("TestElement.test_class", BackendListener.class.getName()));
        backendListener.setProperty(new StringProperty("TestElement.enabled", "true"));
        //backendListener.setProperty(new StringProperty("classname", LoggerBackendListenerClient.class.getName()));

        return backendListener;
    }

    private static BackendListener getInfluxBackendListener() {
        BackendListener backendListener = new BackendListener();
        backendListener.setProperty(new StringProperty("TestElement.name", "InfluxListener"));
        backendListener.setProperty(new StringProperty("TestElement.gui_class", BackendListenerGui.class.getName()));
        backendListener.setProperty(new StringProperty("TestElement.test_class", BackendListener.class.getName()));
        backendListener.setProperty(new StringProperty("TestElement.enabled", "true"));

        Arguments arguments = new Arguments();
        arguments.setProperty(new StringProperty("TestElement.type", "Arguments"));
        arguments.setProperty(new StringProperty("TestElement.gui_class", ArgumentsPanel.class.getName()));
        arguments.setProperty(new StringProperty("TestElement.test_class", Arguments.class.getName()));
        arguments.setProperty(new StringProperty("TestElement.enabled", "true"));

        List<Argument> argumentList = new ArrayList<>();
        Argument influxdbMetricsSender = new Argument();
        influxdbMetricsSender.setProperty(new StringProperty("Argument.name", "influxdbMetricsSender"));
        influxdbMetricsSender.setProperty(new StringProperty("Argument.value", "org.apache.jmeter.visualizers.backend.influxdb.HttpMetricsSender"));
        influxdbMetricsSender.setProperty(new StringProperty("Argument.metadata", "="));

        Argument influxdbUrl = new Argument();
        influxdbUrl.setProperty(new StringProperty("Argument.name", "influxdbUrl"));
        influxdbUrl.setProperty(new StringProperty("Argument.value", "http://47.108.119.100:8086/write?db=catlin"));
        influxdbUrl.setProperty(new StringProperty("Argument.metadata", "="));

        Argument application = new Argument();
        application.setProperty(new StringProperty("Argument.name", "application"));
        application.setProperty(new StringProperty("Argument.value", "app1"));
        application.setProperty(new StringProperty("Argument.metadata", "="));

        Argument measurement = new Argument();
        measurement.setProperty(new StringProperty("Argument.name", "measurement"));
        measurement.setProperty(new StringProperty("Argument.value", "jmeter"));
        measurement.setProperty(new StringProperty("Argument.metadata", "="));

        Argument summaryOnly = new Argument();
        summaryOnly.setProperty(new StringProperty("Argument.name", "summaryOnly"));
        summaryOnly.setProperty(new StringProperty("Argument.value", "false"));
        summaryOnly.setProperty(new StringProperty("Argument.metadata", "="));

        Argument samplersRegex = new Argument();
        samplersRegex.setProperty(new StringProperty("Argument.name", "samplersRegex"));
        samplersRegex.setProperty(new StringProperty("Argument.value", ".*"));
        samplersRegex.setProperty(new StringProperty("Argument.metadata", "="));

        Argument percentiles = new Argument();
        percentiles.setProperty(new StringProperty("Argument.name", "percentiles"));
        percentiles.setProperty(new StringProperty("Argument.value", "99;95;90"));
        percentiles.setProperty(new StringProperty("Argument.metadata", "="));

        Argument testTitle = new Argument();
        testTitle.setProperty(new StringProperty("Argument.name", "testTitle"));
        testTitle.setProperty(new StringProperty("Argument.value", "app1"));
        testTitle.setProperty(new StringProperty("Argument.metadata", "="));

        Argument eventTags = new Argument();
        eventTags.setProperty(new StringProperty("Argument.name", "eventTags"));
        eventTags.setProperty(new StringProperty("Argument.value", ""));
        eventTags.setProperty(new StringProperty("Argument.metadata", "="));

        argumentList.add(influxdbMetricsSender);
        argumentList.add(influxdbUrl);
        argumentList.add(application);
        argumentList.add(measurement);
        argumentList.add(summaryOnly);
        argumentList.add(samplersRegex);
        argumentList.add(percentiles);
        argumentList.add(testTitle);
        argumentList.add(eventTags);

        arguments.setProperty(new CollectionProperty("Arguments.arguments", argumentList));
        backendListener.setProperty(new TestElementProperty("arguments", arguments));
        backendListener.setProperty(new StringProperty("classname", InfluxdbBackendListenerClient.class.getName()));
        return backendListener;
    }

    private static List<ResultCollector> getResultCollector() {
        List<ResultCollector> resultCollectors = new ArrayList<>();

        // 察看结果树
        Summariser summariser = new Summariser("速度");
        ResultCollector resultCollector = new ResultCollector(summariser);
        resultCollector.setProperty(new BooleanProperty("ResultCollector.error_logging", false));
        resultCollector.setProperty(new ObjectProperty("saveConfig", getSampleSaveConfig()));
        resultCollector.setProperty(new StringProperty("TestElement.gui_class", "org.apache.jmeter.visualizers.ViewResultsFullVisualizer"));
        resultCollector.setProperty(new StringProperty("TestElement.name", "察看结果树"));
        resultCollector.setProperty(new StringProperty("TestElement.enabled", "true"));
        resultCollector.setProperty(new StringProperty("filename", replayLogPath));
        resultCollectors.add(resultCollector);

        // 结果汇总
        ResultCollector resultTotalCollector = new ResultCollector();
        resultTotalCollector.setProperty(new BooleanProperty("ResultCollector.error_logging", false));
        resultTotalCollector.setProperty(new ObjectProperty("saveConfig", getSampleSaveConfig()));
        resultTotalCollector.setProperty(new StringProperty("TestElement.gui_class", "org.apache.jmeter.visualizers.SummaryReport"));
        resultTotalCollector.setProperty(new StringProperty("TestElement.name", "汇总报告"));
        resultTotalCollector.setProperty(new StringProperty("TestElement.enabled", "true"));
        resultTotalCollector.setProperty(new StringProperty("filename", summaryLogPath));
        resultCollectors.add(resultTotalCollector);

        return resultCollectors;
    }

    private static SampleSaveConfiguration getSampleSaveConfig() {
        SampleSaveConfiguration sampleSaveConfiguration = new SampleSaveConfiguration();
        sampleSaveConfiguration.setTime(true);
        sampleSaveConfiguration.setLatency(true);
        sampleSaveConfiguration.setTimestamp(true);
        sampleSaveConfiguration.setSuccess(true);
        sampleSaveConfiguration.setLabel(true);
        sampleSaveConfiguration.setCode(true);
        sampleSaveConfiguration.setMessage(true);
        sampleSaveConfiguration.setThreadName(true);
        sampleSaveConfiguration.setDataType(true);
        sampleSaveConfiguration.setEncoding(false);
        sampleSaveConfiguration.setAssertions(true);
        sampleSaveConfiguration.setSubresults(true);
        sampleSaveConfiguration.setResponseData(true);
        sampleSaveConfiguration.setSamplerData(true);
        sampleSaveConfiguration.setAsXml(false);
        sampleSaveConfiguration.setFieldNames(true);
        sampleSaveConfiguration.setResponseHeaders(false);
        sampleSaveConfiguration.setRequestHeaders(false);
        //sampleSaveConfiguration.setAssertionResultsFailureMessage(true);  responseDataOnError
        sampleSaveConfiguration.setAssertionResultsFailureMessage(true);
        //sampleSaveConfiguration.setsserAtionsResultsToSave(0); assertionsResultsToSave
        sampleSaveConfiguration.setBytes(true);
        sampleSaveConfiguration.setSentBytes(true);
        sampleSaveConfiguration.setUrl(true);
        sampleSaveConfiguration.setThreadCounts(true);
        sampleSaveConfiguration.setIdleTime(true);
        sampleSaveConfiguration.setConnectTime(true);
        return sampleSaveConfiguration;
    }

    /***
     * 创建http请求信息
     * @param url ip地址
     * @param port 端口
     * @param api url
     * @param request 请求参数（请求体）
     * @return
     */
    private static HTTPSamplerProxy getHttpSamplerProxy(String url, String port, String api, String request,String method) {
        HTTPSamplerProxy httpSamplerProxy = new HTTPSamplerProxy();
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.domain", url));
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.port", port));
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.protocol", "http"));
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.path", api+"?"+request));
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.method", method));
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.contentEncoding", JMETER_ENCODING));
        httpSamplerProxy.setProperty(new BooleanProperty("HTTPSampler.follow_redirects", true));
        httpSamplerProxy.setProperty(new BooleanProperty("HTTPSampler.postBodyRaw", true));
        httpSamplerProxy.setProperty(new BooleanProperty("HTTPSampler.auto_redirects", false));
        httpSamplerProxy.setProperty(new BooleanProperty("HTTPSampler.use_keepalive", true));
        httpSamplerProxy.setProperty(new BooleanProperty("HTTPSampler.DO_MULTIPART_POST", false));
        httpSamplerProxy.setProperty(new StringProperty("TestElement.gui_class", "org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui"));
        httpSamplerProxy.setProperty(new StringProperty("TestElement.test_class", "org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy"));
        httpSamplerProxy.setProperty(new StringProperty("TestElement.name", "HealthRequest"));
        httpSamplerProxy.setProperty(new StringProperty("TestElement.enabled", "true"));
        httpSamplerProxy.setProperty(new BooleanProperty("HTTPSampler.postBodyRaw", true));
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.embedded_url_re", ""));
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.connect_timeout", ""));
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.response_timeout", ""));
        return httpSamplerProxy;
    }

    /***
     * 创建线程组
     * @param loopController 循环控制器
     * @param numThreads 线程数量
     * @return
     */
    private static ThreadGroup getThreadGroup(LoopController loopController, int numThreads) {
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setNumThreads(numThreads);
        threadGroup.setRampUp(1);
        threadGroup.setDelay(0);
        threadGroup.setDuration(runCount);
        threadGroup.setProperty(new StringProperty(ThreadGroup.ON_SAMPLE_ERROR, "continue"));
        threadGroup.setScheduler(true);
        threadGroup.setName("ThreadGroup");
        threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
        threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());
        threadGroup.setProperty(new BooleanProperty(TestElement.ENABLED, true));
        threadGroup.setProperty(new TestElementProperty(ThreadGroup.MAIN_CONTROLLER, loopController));
        return threadGroup;
    }

    private static LoopController getLoopController() {
        LoopController loopController = new LoopController();
        loopController.setContinueForever(false);
        loopController.setProperty(new StringProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName()));
        loopController.setProperty(new StringProperty(TestElement.TEST_CLASS, LoopController.class.getName()));
        loopController.setProperty(new StringProperty(TestElement.NAME, "循环控制器"));
        loopController.setProperty(new StringProperty(TestElement.ENABLED, "true"));
        loopController.setProperty(new StringProperty(LoopController.LOOPS, "-1"));
        return loopController;
    }

    private static TestPlan getTestPlan() {
        TestPlan testPlan = new TestPlan("MingTestPlan");
        testPlan.setFunctionalMode(false);
        testPlan.setSerialized(false);
        testPlan.setTearDownOnShutdown(true);
        testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
        testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
        testPlan.setProperty(new BooleanProperty(TestElement.ENABLED, true));
        testPlan.setProperty(new StringProperty(TestElement.COMMENTS, "MingTestPlan"));
        testPlan.setTestPlanClasspath("");
        Arguments arguments = new Arguments();
        testPlan.setUserDefinedVariables(arguments);
        return testPlan;
    }

    /**
     * 设置请求头信息
     * @return
     */
    private static HeaderManager getHeaderManager() {
        ArrayList<TestElementProperty> headerMangerList = new ArrayList<>();
        HeaderManager headerManager = new HeaderManager();
        Header header = new Header("Content-Type", "application/json");
        TestElementProperty HeaderElement = new TestElementProperty("", header);
        headerMangerList.add(HeaderElement);

        headerManager.setEnabled(true);
        headerManager.setName("HTTP Header Manager");
        headerManager.setProperty(new CollectionProperty(HeaderManager.HEADERS, headerMangerList));
        headerManager.setProperty(new StringProperty(TestElement.TEST_CLASS, HeaderManager.class.getName()));
        headerManager.setProperty(new StringProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName()));
        return headerManager;
    }

    private static CSVDataSet getCSVDataSet(String bodyPath) {
        CSVDataSet csvDataSet = new CSVDataSet();
        csvDataSet.setEnabled(true);
        csvDataSet.setName("body请求体");
        csvDataSet.setProperty(TestElement.TEST_CLASS, CSVDataSet.class.getName());
        csvDataSet.setProperty(TestElement.GUI_CLASS, TestBeanGUI.class.getName());

        csvDataSet.setProperty(new StringProperty("filename", bodyPath));
        csvDataSet.setProperty(new StringProperty("fileEncoding", JMETER_ENCODING));
        csvDataSet.setProperty(new BooleanProperty("ignoreFirstLine", false));
        csvDataSet.setProperty(new BooleanProperty("quotedData", true));
        csvDataSet.setProperty(new BooleanProperty("recycle", false));
        csvDataSet.setProperty(new BooleanProperty("stopThread", false));
        csvDataSet.setProperty(new StringProperty("variableNames", ""));
        csvDataSet.setProperty(new StringProperty("shareMode", CSVDataSetBeanInfo.getShareTags()[0]));
        csvDataSet.setProperty(new StringProperty("delimiter", ","));
        return csvDataSet;
    }

    /***
     * 限制QPS设置
     * @param throughputTimer
     * @return
     */
    private static ConstantThroughputTimer getConstantThroughputTimer(int throughputTimer) {
        ConstantThroughputTimer constantThroughputTimer = new ConstantThroughputTimer();
        constantThroughputTimer.setEnabled(true);
        constantThroughputTimer.setName("常数吞吐量定时器");
        constantThroughputTimer.setProperty(TestElement.TEST_CLASS, ConstantThroughputTimer.class.getName());
        constantThroughputTimer.setProperty(TestElement.GUI_CLASS, TestBeanGUI.class.getName());
        constantThroughputTimer.setCalcMode(ConstantThroughputTimer.Mode.AllActiveThreads.ordinal());

        constantThroughputTimer.setProperty(new IntegerProperty("calcMode", ConstantThroughputTimer.Mode.AllActiveThreads.ordinal()));
        DoubleProperty doubleProperty = new DoubleProperty();
        doubleProperty.setName("throughput");
        doubleProperty.setValue(throughputTimer * 60f);
        constantThroughputTimer.setProperty(doubleProperty);
        return constantThroughputTimer;
    }
}
