package com.sjbaek.chapter3;

import com.sjbaek.chapter3.domain.PriceUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import java.util.Locale;

@Slf4j
@SpringBootApplication
public class SpringBean09Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(SpringBean09Application.class, args);
        log.info("------ Done to initialize spring beans");
        PriceUnit priceUnit = context.getBean("lazyPriceUnit", PriceUnit.class);
        log.info("Locale in PriceUnit : {}", priceUnit.getLocale().toString());
        context.close();
    }

    @Lazy
    @Bean
    public PriceUnit lazyPriceUnit() {
        log.info("initialize lazyPriceUnit");
        return new PriceUnit(Locale.US);
    }
}
