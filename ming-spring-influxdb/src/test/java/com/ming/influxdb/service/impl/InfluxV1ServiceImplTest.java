package com.ming.influxdb.service.impl;

import com.alibaba.fastjson.JSON;
import com.ming.influxdb.InfluxdbBaseTest;
import com.ming.influxdb.model.Cpu;
import com.ming.influxdb.model.InfluxModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class InfluxV1ServiceImplTest extends InfluxdbBaseTest {

    @Autowired
    private InfluxV1ServiceImpl influxV1Service;

    @Test
    public void getVersion() {
        String version = influxV1Service.getVersion();
        System.out.println(version);
    }

    @Test
    public void ping() {
        System.out.println(influxV1Service.ping());
    }

    @Test
    public void getMeasurementList() {
        String sql = "show measurements";
        List<Map<String, Object>> rowList = influxV1Service.query(sql);
        for (Map<String, Object> row : rowList) {
            System.out.println(row);
        }
    }

    @Test
    public void query() {
        String sql = "select * from cpu";
        List<Cpu> rowList = influxV1Service.query(sql, Cpu.class);
        for (Cpu cpu : rowList) {
            //System.out.println(JSON.toJSONString(cpu));
        }
        System.out.println(rowList.size());
    }

    @Test
    public void write() throws Exception {
        Cpu cpu = new Cpu();
        cpu.setRegion("cn-hangzhou-01");
        cpu.setHost("host-01");
        cpu.setValue(0.1);

        influxV1Service.write(cpu);
    }

    @Test
    public void isBatchEnabled() {
        System.out.println(influxV1Service.isBatchEnabled());
    }

    @Test
    public void batchWrite() {
        long start = System.currentTimeMillis();

        List<InfluxModel> cpuList = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.00");
        Random random = new Random(System.currentTimeMillis());

        for(int i=1; i<=100000; i++) {
            Cpu cpu = new Cpu();
            int index = random.nextInt(100) + 100;
            cpu.setRegion("cn-hangzhou-" + index);
            cpu.setHost("host-" + index);
            double value = Double.parseDouble(df.format(random.nextDouble()));
            cpu.setValue(value);
            cpuList.add(cpu);
        }

        influxV1Service.batchWrite(cpuList);
        long end = System.currentTimeMillis();
        System.out.println("batch write time cost: " + (end - start));
        System.out.println("data size: " + cpuList.size());

    }
}