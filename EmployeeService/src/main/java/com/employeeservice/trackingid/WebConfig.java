package com.employeeservice.trackingid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final SessionMdcInterceptor sessionMdcInterceptor;

    @Autowired
    public WebConfig(SessionMdcInterceptor sessionMdcInterceptor) {
        this.sessionMdcInterceptor = sessionMdcInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionMdcInterceptor)
                .addPathPatterns("/**") // Apply to all endpoints
                .excludePathPatterns("/static/**"); // Exclude static resources
    }
}