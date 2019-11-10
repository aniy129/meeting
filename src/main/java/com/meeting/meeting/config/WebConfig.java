package com.meeting.meeting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private ManagerLoginIntercept managerLoginIntercept;

    @Resource
    private UserLoginIntercept userLoginIntercept;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(managerLoginIntercept)
                .addPathPatterns("/manager/**")
                .excludePathPatterns("/",
                        "/login",
                        "/css/**",
                        "/fonts/**",
                        "/images/**",
                        "/js/**",
                        "/node_modules/**",
                        "/scss/**",
                        "/html/**",
                        "/manager/login"
                );
        registry.addInterceptor(userLoginIntercept)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/login");
    }
}
