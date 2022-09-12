package com.ming.boot.intergration.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AliyunConfiguration {
    @Value("${aliyun.oss.endpoint}")
    private String ossEndpoint;

    @Value("${aliyun.oss.accessKey}")
    private String accessKey;

    @Value("${aliyun.oss.accessSecret}")
    private String accessSecret;
}
