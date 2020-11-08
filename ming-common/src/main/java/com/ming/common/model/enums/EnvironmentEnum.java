package com.ming.common.model.enums;

import com.ming.common.exception.EnvironmentException;

public enum EnvironmentEnum {
    DEV("DEV"),
    GAMMA("GAMMA"),
    PROD("PROD")
    ;
    private String env;

    EnvironmentEnum(String env) {
        this.env = env;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public static EnvironmentEnum getEnvironment(String env) {
        for (EnvironmentEnum environmentEnum : EnvironmentEnum.values()) {
            String value = environmentEnum.getEnv();
            if(value.equalsIgnoreCase(env)) {
                return environmentEnum;
            }
        }

        throw new EnvironmentException("unkown environment: " + env);
    }
}
