package com.stardata.starshop2.ordercontext.command;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/6 11:07
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.stardata.starshop2"})
//@EnableDubbo
@ComponentScan(basePackages = {"com.stardata.starshop2"})
public class Starshop2OrderCommandApplication {
    public static void main(String[] args) {
        SpringApplication.run(Starshop2OrderCommandApplication.class, args);
    }
}


