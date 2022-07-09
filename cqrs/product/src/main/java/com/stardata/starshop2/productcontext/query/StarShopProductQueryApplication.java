package com.stardata.starshop2.productcontext.query;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.stardata.starshop2.productcontext.query.mapper")
public class StarShopProductQueryApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarShopProductQueryApplication.class, args);
    }
}

