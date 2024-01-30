package com.stardata.starshop2.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.stardata.starshop2"})
@ComponentScan(basePackages = {"com.stardata.starshop2"})
@EnableJpaRepositories(basePackages = "com.stardata.starshop2")
@EnableTransactionManagement(proxyTargetClass = true)
@EntityScan(basePackages = "com.stardata.starshop2")
public class SimulatorOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimulatorOrderApplication.class, args);
    }

}
