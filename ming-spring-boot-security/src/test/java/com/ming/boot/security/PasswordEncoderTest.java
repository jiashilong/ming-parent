package com.ming.boot.security;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author shilong.jia
 * @version 1.0.0
 * @createTime 2021-11-24
 * @Description
 */
public class PasswordEncoderTest extends BaseTest{
    private PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Test
    public void encode() {
        String p1 = encoder.encode("cc");
        System.out.println(p1);
    }

    @Test
    public void base64Encode() {
        String username = "aa";
        //String password = "$2a$10$yoezR0LSKGKiddDsnfpB7OXWJEA8/ulC5Eq3rnGHN912oLz2XVadK";
        String password = "bb";
        String auth = username + ":" + password + "-cc" + "_dd";
        String base64Auth = Base64.encodeBase64URLSafeString(auth.getBytes());
        System.out.println(base64Auth);
    }

    @Test
    public void base64Decode() throws UnsupportedEncodingException {
        String base64Auth = "YWE6YmItY2NfZGQ";
        byte[] bytes = Base64.decodeBase64(base64Auth.getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(bytes, "UTF-8"));
    }

    @Test
    public void test1() {

    }
}
