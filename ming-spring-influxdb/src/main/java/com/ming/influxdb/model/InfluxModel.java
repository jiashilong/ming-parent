package com.ming.influxdb.model;

import org.influxdb.annotation.Column;

import java.io.Serializable;


public class InfluxModel implements Serializable {
    @Column(name = "milliseconds", tag = true)
    private String milliseconds = String.valueOf(System.currentTimeMillis());

    @Column(name = "nanoseconds", tag = true)
    private String nanoseconds = String.valueOf(System.nanoTime());

    public String getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(String milliseconds) {
        this.milliseconds = milliseconds;
    }

    public String getNanoseconds() {
        return nanoseconds;
    }

    public void setNanoseconds(String nanoseconds) {
        this.nanoseconds = nanoseconds;
    }
}
