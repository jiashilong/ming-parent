package com.ming.boot.intergration.service;

import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.ObjectListing;

import java.util.List;

public interface AliyunOSSService {
    List<Bucket> listBuckets();
    ObjectListing listObjects(String bucketName);
}
