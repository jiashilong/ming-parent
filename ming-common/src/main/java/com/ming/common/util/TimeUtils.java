package com.ming.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String now() {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DEFAULT_FORMAT);
        return timeFormatter.format(time);
    }
}
