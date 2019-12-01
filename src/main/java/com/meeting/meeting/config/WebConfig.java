package com.meeting.meeting.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;

@Configuration
@EnableWebMvc
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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.
                addResourceHandler("/**").
                addResourceLocations("classpath:/static/", "file:static/");
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config); // CORS 配置对所有接口都有效
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
}
