package com.wyj.configuration;

import feign.Contract;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;


/**
 * 启动FeignClient扫描，并配置：
 * 1. 并指定包包扫描地址
 * 2. 设置交互为JAX-RS2方式，实际Feign中的JAX-RS2指的是1.1
 * 3. 在请求时自动加入基于OAuth2的客户端模式认证的Header
 *
 * @author icyfenix@gmail.com
 * @date 2020/4/18 22:38
 **/
@Configuration
@EnableFeignClients(basePackages = {"com.wyj"})
public class FeignConfiguration {

    @Autowired
    private ClientCredentialsResourceDetails resource;

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), resource);
    }

}
