package com.ming.boot.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author shilong.jia
 * @version 1.0.0
 * @createTime 2021-11-24
 * @Description
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("aa").password("$2a$10$yoezR0LSKGKiddDsnfpB7OXWJEA8/ulC5Eq3rnGHN912oLz2XVadK").roles()
                .and()
                .withUser("bb").password("$2a$10$fYPRHRbEfQtji1wGPDXUiuEvOplB9rAtidqiGO35tOBz3vonWZk8a").roles()
                .and()
                .withUser("cc").password("$2a$10$FdNjVjfKWmo/VGAN.etBuOMpBb1ndyadT55mCJycZRizxiF3ou8uW");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
