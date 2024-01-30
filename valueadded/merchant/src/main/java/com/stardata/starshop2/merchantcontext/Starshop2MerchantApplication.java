package com.stardata.starshop2.merchantcontext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/11 10:43
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.stardata.starshop2"})
@EnableDiscoveryClient
//@EnableDubbo
public class Starshop2MerchantApplication {
    public static void main(String[] args) {
        SpringApplication.run(Starshop2MerchantApplication.class, args);
    }
}


