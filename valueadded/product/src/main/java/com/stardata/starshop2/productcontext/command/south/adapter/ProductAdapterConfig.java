package com.stardata.starshop2.productcontext.command.south.adapter;

import com.stardata.starshop2.productcontext.command.domain.product.Product;
import com.stardata.starshop2.productcontext.command.domain.productcategory.ProductCategory;
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
public class ProductAdapterConfig {
    @Resource
    EntityManager entityManager;

    @Bean
    GenericRepository<Product, LongIdentity> createOrderRepository() {
        return new GenericRepository<>(Product.class, entityManager);
    }

    @Bean
    GenericRepository<ProductCategory, LongIdentity> createShoppingCartRepository() {
        return new GenericRepository<>(ProductCategory.class, entityManager);
    }
}
