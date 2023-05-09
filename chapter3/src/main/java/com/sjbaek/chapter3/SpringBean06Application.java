package com.sjbaek.chapter3;

import com.sjbaek.chapter3.domain.lifecycle.LifeCycleComponent;
import com.sjbaek.chapter3.domain.lifecycle.PrintableBeanPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBean06Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBean06Application.class, args);
        context.close();
    }

    @Bean(initMethod = "init", destroyMethod = "clear")
    public LifeCycleComponent lifecycleComponent() {
        return new LifeCycleComponent();
    }

    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new PrintableBeanPostProcessor();
    }
}
