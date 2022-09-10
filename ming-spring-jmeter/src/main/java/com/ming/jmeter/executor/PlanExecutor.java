package com.ming.jmeter.executor;

public interface PlanExecutor {
    String ENV_JMETER_HOME = "JMETER_HOME";
    String DEFAULT_JMETER_BIN = "bin";
    String DEFAULT_JMETER_REPORT = "report";
    String DEFAULT_JMETER_PROPERTIES = "jmeter.properties";
    String DEFAULT_JMETER_REPORT_JTL = "report.jtl";
    int DEFAULT_LOOP_COUNT = -1;

    default String getJMeterHomePath() {
        return System.getenv(ENV_JMETER_HOME);
    }

    default String getFileSeparator() {
        return System.getProperty("file.separator", "/");
    }

    default String getJMeterProperty() {
        return DEFAULT_JMETER_PROPERTIES;
    }

    default String getJMeterBin() {
        return getJMeterHomePath() + getFileSeparator() + DEFAULT_JMETER_BIN;
    }

    default String getJMeterPropertyPath() {
        return getJMeterBin() + this.getFileSeparator() + getJMeterProperty();
    }

    int getThreadNum();
    int getDuration();
    String getName();
    default int getLoops() {
        return DEFAULT_LOOP_COUNT;
    }
    default String getReport() {
        return this.getJMeterHomePath() + this.getFileSeparator()
                + DEFAULT_JMETER_REPORT + this.getFileSeparator()
                + this.getName() + this.getFileSeparator()
                + DEFAULT_JMETER_REPORT_JTL;
    }
}
