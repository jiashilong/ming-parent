package com.ming.influxdb.service.impl;

import com.ming.influxdb.configuration.InfluxConfiguration;
import com.ming.influxdb.model.InfluxModel;
import com.ming.influxdb.service.InfluxService;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.*;
import org.influxdb.impl.InfluxDBResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class InfluxV1ServiceImpl implements InfluxService {
    private static final Logger logger = LoggerFactory.getLogger(InfluxV1ServiceImpl.class);
    private static final InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
    private static final int DEFAULT_ACTIONS = 1000;
    private static final int DEFAULT_FLUSH_DURATION = 120000;

    @Autowired
    private InfluxConfiguration configuration;

    private InfluxDB influxDB;

    @PostConstruct
    public void init() {
        if(influxDB == null) {
            //influxDB = InfluxDBFactory.connect(configuration.getUrl(), configuration.getUsername(), configuration.getPassword());
            influxDB = InfluxDBFactory.connect(configuration.getUrl());
        }
        influxDB.setDatabase(configuration.getDatabase());
        //influxDB.enableBatch(DEFAULT_ACTIONS, DEFAULT_FLUSH_DURATION, TimeUnit.MILLISECONDS);
        influxDB.enableBatch(BatchOptions.DEFAULTS
                .actions(DEFAULT_ACTIONS)
                .flushDuration(DEFAULT_FLUSH_DURATION)
                .bufferLimit(10)
                .exceptionHandler((points, e) -> {
                    List<Point> target = new ArrayList<>();
                    points.forEach(target::add);
                    String msg = String.format("failed to write points:%s\n", target.toString().substring(0, 10000));
                    logger.error(msg, e);
                })
                .threadFactory(Executors.defaultThreadFactory()));
    }

    @Override
    public String getVersion() {
        return influxDB.version();
    }

    @Override
    public boolean ping() {
        boolean isConnected = false;
        Pong pong = null;
        try {
            pong = influxDB.ping();
            isConnected = true;
        } catch (Exception e) {
            logger.error("influx ping", e);
        }
        return isConnected;
    }

    @Override
    public boolean isBatchEnabled() {
        return influxDB.isBatchEnabled();
    }

    @Override
    public <T> List<T> query(String sql, Class<T> clazz) {
        List<T> rowList = new ArrayList<>();
        if(StringUtils.isEmpty(sql)) {
            return rowList;
        }

        QueryResult queryResult = influxDB.query(new Query(sql));
        if(queryResult == null) {
            return rowList;
        }

        rowList = resultMapper.toPOJO(queryResult, clazz);
        return rowList;
    }

    @Override
    public List<Map<String, Object>> query(String sql) {
        List<Map<String, Object>> rowList = new ArrayList<>();
        if(StringUtils.isEmpty(sql)) {
            return rowList;
        }

        QueryResult queryResult = influxDB.query(new Query(sql));
        if(queryResult == null) {
            return rowList;
        }

        rowList = this.format(queryResult);
        return rowList;
    }

    @Override
    public void write(InfluxModel model) {
        if(model == null) {
            return;
        }

//        if(model.getTime() == null) {
//            model.setTime(System.nanoTime());
//        }

        Point point = Point.measurementByPOJO(model.getClass())
                .addFieldsFromPOJO(model)
                .build();
        influxDB.write(point);
        influxDB.flush();
    }

    @Override
    public void batchWrite(List<InfluxModel> modelList) {
        if(CollectionUtils.isEmpty(modelList)) {
            return ;
        }

        BatchPoints batchPoints = BatchPoints.builder().build();
        for (InfluxModel model : modelList) {
            Point point = Point.measurementByPOJO(model.getClass())
                    .addFieldsFromPOJO(model)
                    .build();
            batchPoints.point(point);
        }

        influxDB.write(batchPoints);
        influxDB.flush();

    }

    private List<Map<String, Object>> format(QueryResult queryResult) {
        List<Map<String, Object>> rowList = new ArrayList<>();
        if(queryResult.hasError()) {
            return rowList;
        }

        List<QueryResult.Result> resultList = queryResult.getResults();
        if(CollectionUtils.isEmpty(resultList)) {
            return rowList;
        }

        for (QueryResult.Result result : resultList) {
            List<QueryResult.Series> seriesList = result.getSeries();
            if(CollectionUtils.isEmpty(seriesList)) {
                continue;
            }

            for (QueryResult.Series series : seriesList) {
                series.getColumns();
                List<List<Object>> valueList = series.getValues();
                List<String> columnList = series.getColumns();

                for(int i=0; i<valueList.size(); i++) {
                    Map<String, Object> dataMap = new HashMap<>();
                    for(int j=0; j<columnList.size(); j++) {
                        String key = columnList.get(j);
                        Object value = valueList.get(i).get(j);
                        dataMap.put(key, value);
                    }
                    rowList.add(dataMap);
                }
            }
        }

        return rowList;
    }
}
