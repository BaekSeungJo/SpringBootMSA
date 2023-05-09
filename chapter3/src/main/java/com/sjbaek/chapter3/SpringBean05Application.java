package com.sjbaek.chapter3;

import com.sjbaek.chapter3.domain.format.DateFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
public class SpringBean05Application {

    public static void main(String[] args) throws InterruptedException{
        ConfigurableApplicationContext context =
                SpringApplication.run(SpringBean05Application.class, args);
        ThreadPoolTaskExecutor taskExecutor =
                context.getBean(ThreadPoolTaskExecutor.class);

        final String dateString = "2020-12-24T23:59:59.-08:00";
        for(int i = 0; i < 100; i++) {
            taskExecutor.execute(() -> {
                try {
                    DateFormatter formatter = context.getBean("singletonDateFormatter", DateFormatter.class);
                    log.info("Date : {}, hasCode : {}", formatter.parse(dateString), formatter.hashCode());
                } catch (Exception e) {
                    log.error("error to parse", e);
                }
            });
        }
        TimeUnit.SECONDS.sleep(5);
        context.close();
    }

    @Bean
    public DateFormatter singletonDateFormatter() {
        return new DateFormatter("yyyy-MM-dd'T'HH:mm:ss");
    }
}
