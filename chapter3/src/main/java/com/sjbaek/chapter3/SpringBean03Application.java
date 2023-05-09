package com.sjbaek.chapter3;

import com.sjbaek.chapter3.domain.ProductOrder;
import com.sjbaek.chapter3.service.Printer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@SpringBootApplication
public class SpringBean03Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(SpringBean03Application.class, args);

        Printer printer = context.getBean(Printer.class);
        ProductOrder order = new ProductOrder(BigDecimal.valueOf(1000), LocalDateTime.now(), "sjbaek");

        try(OutputStream os = System.out) {
            printer.print(os, order);
        } catch (IOException ioe) {
            log.error("Error to print", ioe);
        }
    }
}
