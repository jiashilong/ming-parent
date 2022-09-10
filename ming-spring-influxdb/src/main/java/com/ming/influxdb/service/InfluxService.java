package com.ming.influxdb.service;

import com.ming.influxdb.model.InfluxModel;

import java.util.List;
import java.util.Map;

public interface InfluxService {
    String getVersion();
    boolean ping();
    boolean isBatchEnabled();

    <T> List<T> query(String sql, Class<T> clazz);
    List<Map<String, Object>> query(String sql);

    void write(InfluxModel model);
    void batchWrite(List<InfluxModel> modelList);
}
