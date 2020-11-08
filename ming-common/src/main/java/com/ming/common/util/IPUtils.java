package com.ming.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

public class IPUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(IPUtils.class);

    public static String getLocalAddress() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (Exception e) {
            LOGGER.error("get local address exception", e);
        }

        return "";
    }
}
