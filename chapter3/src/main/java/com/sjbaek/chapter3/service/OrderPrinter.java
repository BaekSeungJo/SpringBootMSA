package com.sjbaek.chapter3.service;

import com.sjbaek.chapter3.domain.ProductOrder;
import com.sjbaek.chapter3.domain.format.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;

@Service
public class OrderPrinter implements Printer<ProductOrder>{

    // 빈 필드 주입
    @Autowired
    @Qualifier("localDateTimeFormatter")
    private Formatter formatter01;


    // setter 메서드 빈 주입
    private Formatter formatter02;

    @Autowired
    public void setFormatter02(@Qualifier("localDateTimeFormatter") Formatter formatter) {
        this.formatter02 = formatter;
    }

    private Formatter formatter03;

    // 빈 생성자 주입
    @Autowired
    public OrderPrinter(@Qualifier("localDateTimeFormatter") Formatter formatter03) {
        this.formatter03 = formatter03;
    }

    @Override
    public void print(OutputStream os, ProductOrder productOrder) throws IOException {
        String orderAt = formatter01.of(productOrder.getOrderAt());
        os.write(orderAt.getBytes());
    }
}
