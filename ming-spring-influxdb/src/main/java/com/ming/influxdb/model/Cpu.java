package com.ming.influxdb.model;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "cpu")
public class Cpu extends InfluxModel {
    @Column(name = "region", tag = true)
    private String region;

    @Column(name = "host", tag = true)
    private String host;

    @Column(name = "value")
    private Double value;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
