package com.stardata.starshop2.productcontext.command.south.adapter;

import com.stardata.starshop2.productcontext.command.domain.product.Product;
import com.stardata.starshop2.productcontext.command.domain.productcategory.ProductCategory;
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
public class ProductAdapterConfig {
    @Resource
    EntityManager entityManager;

    @Bean
    GenericRepository<Product, LongIdentity> createProductRepository() {
        return new GenericRepository<>(Product.class, entityManager);
    }

    @Bean
    GenericRepository<ProductCategory, LongIdentity> createProductCategoryRepository() {
        return new GenericRepository<>(ProductCategory.class, entityManager);
    }
}
