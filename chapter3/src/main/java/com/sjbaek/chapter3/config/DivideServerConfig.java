package com.sjbaek.chapter3.config;

import com.sjbaek.chapter3.domain.format.DateFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DivideServerConfig {

    @Bean
    public DateFormatter localDateFormatter(String localDatePattern) {
        return new DateFormatter(localDatePattern);
    }
}
