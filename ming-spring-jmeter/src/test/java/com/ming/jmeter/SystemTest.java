package com.ming.jmeter;


import org.junit.Test;

public class SystemTest extends BaseTest {

    @Test
    public void env() {
        String home = System.getenv("JMETER_HOME");
        System.out.println(home);
    }

    @Test
    public void test() {

    }
}
