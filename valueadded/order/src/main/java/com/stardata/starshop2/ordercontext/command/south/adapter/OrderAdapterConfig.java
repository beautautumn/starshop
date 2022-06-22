package com.stardata.starshop2.ordercontext.command.south.adapter;

import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCart;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.south.adapter.GenericRepository;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/19 21:39
 */
@Configuration
@ComponentScan
public class OrderAdapterConfig {
    @Resource
    EntityManager entityManager;

    @Bean
    GenericRepository<Order, LongIdentity> createOrderRepository() {
        return new GenericRepository<>(Order.class, entityManager);
    }

    @Bean
    GenericRepository<ShoppingCart, LongIdentity> createShoppingCartRepository() {
        return new GenericRepository<>(ShoppingCart.class, entityManager);
    }
}
