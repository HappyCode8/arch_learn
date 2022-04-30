package com.wyj.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 移除对静态资源的安全控制，禁止其它的缓存
 **/
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //不允许缓存，每次都去服务器拿最新的值
        http.headers().cacheControl().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        //忽略Spring Security对静态资源的控制
        web.ignoring().antMatchers("/static/**");
    }
}

