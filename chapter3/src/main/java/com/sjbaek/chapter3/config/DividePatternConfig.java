package com.sjbaek.chapter3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DividePatternConfig {

    @Bean
    public String localDatePattern() {
        return "yyyy-MM-dd'T'HH:mm:ss";
    }
}
