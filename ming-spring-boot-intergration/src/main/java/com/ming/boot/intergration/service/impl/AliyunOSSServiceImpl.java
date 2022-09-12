package com.ming.boot.intergration.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.ObjectListing;
import com.ming.boot.intergration.configuration.AliyunConfiguration;
import com.ming.boot.intergration.service.AliyunOSSService;
import com.ming.boot.intergration.util.EncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class AliyunOSSServiceImpl implements AliyunOSSService {
    @Autowired
    private AliyunConfiguration configuration;

    private OSS ossCliet;

    @PostConstruct
    public void init() throws UnsupportedEncodingException {
        String endpoint = configuration.getOssEndpoint();
        String accessKey = EncodeUtil.base64Decode(configuration.getAccessKey());
        String accessSecret = EncodeUtil.base64Decode(configuration.getAccessSecret());
        this.ossCliet = new OSSClientBuilder().build(endpoint, EncodeUtil.reverse(accessKey), EncodeUtil.reverse(accessSecret));
    }

    @Override
    public List<Bucket> listBuckets() {
        List<Bucket> buckets = ossCliet.listBuckets();
        return buckets;
    }

    @Override
    public ObjectListing listObjects(String bucketName) {
        ObjectListing objectListing = ossCliet.listObjects(bucketName);
        return objectListing;
    }
}
