package com.ming.common.model;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResultTest {

    @Test
    public void failure() {
        System.out.println(JSON.toJSONString(Result.failure("hehe")));
    }
}