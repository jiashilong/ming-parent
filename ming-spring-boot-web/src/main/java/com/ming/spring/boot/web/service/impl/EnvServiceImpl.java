package com.ming.spring.boot.web.service.impl;

import com.ming.common.model.enums.EnvironmentEnum;
import com.ming.spring.boot.web.service.EnvService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EnvServiceImpl implements EnvService {
    @Value("${spring.profile.active}")
    private String env;

    @Override
    public String getEnvironment() {
        return EnvironmentEnum.getEnvironment(env).getEnv();
    }

    @Override
    public boolean isDaily() {
        EnvironmentEnum environmentEnum = EnvironmentEnum.getEnvironment(env);
        if(environmentEnum.equals(EnvironmentEnum.DEV)) {
            return true;
        }
        return false;
    }
}
