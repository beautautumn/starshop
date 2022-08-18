package com.stardata.starshop2.productcontext.command;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/8/14 10:52
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.stardata.starshop2"})
@EnableDiscoveryClient
@EnableDubbo
public class Starshop2ProductCommandSpringCloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(Starshop2ProductCommandSpringCloudApplication.class, args);
    }
}
