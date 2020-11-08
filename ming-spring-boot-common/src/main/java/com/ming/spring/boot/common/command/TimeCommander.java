package com.ming.spring.boot.common.command;

import com.ming.common.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TimeCommander implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeCommander.class);

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("current time is {}", TimeUtils.now());
    }
}
