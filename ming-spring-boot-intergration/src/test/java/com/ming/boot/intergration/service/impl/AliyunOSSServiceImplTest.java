package com.ming.boot.intergration.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.ObjectListing;
import com.ming.boot.intergration.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class AliyunOSSServiceImplTest extends BaseTest {
    @Autowired
    private AliyunOSSServiceImpl aliyunOSSService;

    @Test
    public void listBuckets() {
        List<Bucket> buckets = aliyunOSSService.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(JSON.toJSONString(bucket));
        }
    }

    @Test
    public void listObjects() {
        ObjectListing objectListing = aliyunOSSService.listObjects("catlin");
        System.out.println(JSON.toJSONString(objectListing));
    }
}