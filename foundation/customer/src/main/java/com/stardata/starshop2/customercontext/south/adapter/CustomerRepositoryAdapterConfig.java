package com.stardata.starshop2.customercontext.south.adapter;

import com.stardata.starshop2.customercontext.domain.customer.Customer;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.south.adapter.GenericRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/19 21:39
 */
@Configuration
public class CustomerRepositoryAdapterConfig {
    @Resource
    private EntityManager entityManager;

    @Bean
    GenericRepository<Customer, LongIdentity> createOrderRepository() {
        return new GenericRepository<>(Customer.class, entityManager);
    }

}
