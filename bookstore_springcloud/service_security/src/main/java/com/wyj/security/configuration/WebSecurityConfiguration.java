package com.wyj.security.configuration;

import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 移除对静态资源的安全控制，禁止其它的缓存
 **/
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        //忽略Spring Security对静态资源的控制
        web.ignoring().antMatchers("/static/**");
    }
}

