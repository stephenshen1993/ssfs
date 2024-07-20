package com.stephenshen.ssfs;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author stephenshen
 * @date 2024/7/20 17:04:58
 */
@Configuration
public class SsfsConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation("/tmp/tomcat");
        return factory.createMultipartConfig();
    }
}
