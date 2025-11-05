package com.auditWriter.config;


import com.auditWriter.interceptor.IDGeneratorInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final IDGeneratorInterceptor idGeneratorInterceptor;

    @Autowired
    public WebConfig(IDGeneratorInterceptor idGeneratorInterceptor) {
        this.idGeneratorInterceptor = idGeneratorInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(idGeneratorInterceptor);
    }
}