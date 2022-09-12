package com.ming.boot.intergration.util;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class EncodeUtil {

    public static String reverse(String str) {
        if(StringUtils.isEmpty(str)) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str);
        return sb.reverse().toString();
    }

    public static String base64Decode(String str) throws UnsupportedEncodingException {
        if(StringUtils.isEmpty(str)) {
            return str;
        }

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(str);
        return new String(bytes, "UTF-8");
    }
}
