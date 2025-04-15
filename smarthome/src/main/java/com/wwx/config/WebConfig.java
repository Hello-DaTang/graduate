package com.wwx.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wwx.interceptor.LoginCheckInterceptor;
//配置类，用于配置拦截器
//因为interceptor是spring内部的组件，所以不需要像filter一样配置在启动类里加@ServletComponentScan
@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**").excludePathPatterns("/login");
    }


}
