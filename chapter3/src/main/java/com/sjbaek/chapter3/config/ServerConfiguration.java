package com.sjbaek.chapter3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.datetime.DateFormatter;

@Configuration
@ComponentScan(
        basePackages = {"com.sjbaek.chapter3.config", "com.sjbaek.chapter3.domain"},
        basePackageClasses = {ServerConfiguration.class}
)
@Import(value = {ThreadPoolConfig.class})
public class ServerConfiguration {

    @Bean
    public String datePattern() {
        return "yyyy-MM-dd'T'HH:mm:ss.XXX";
    }

    @Bean
    public DateFormatter defaultDateFormatter() {
        return new DateFormatter(datePattern());
    }
}
