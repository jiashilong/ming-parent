package com.ming.common.model;

import com.ming.common.util.IPUtils;

public class Result<T> {
    private static final String LOCAL_HOST = IPUtils.getLocalAddress();

    private boolean success;
    private String message;
    private String host;
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        Result result = new Result();
        result.setSuccess(true);
        result.setMessage("");
        result.setData(data);
        result.setHost(LOCAL_HOST);
        return result;
    }

    public static <T> Result<T> failure(String message) {
        Result result = new Result();
        result.setSuccess(false);
        result.setMessage(message);
        result.setHost(LOCAL_HOST);
        result.setData(null);
        return result;
    }
}
