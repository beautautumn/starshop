package com.stardata.starshop2.sharedcontext.south.adapter;

import com.stardata.starshop2.sharedcontext.domain.BizParameter;
import com.stardata.starshop2.sharedcontext.domain.StringIdentity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan
public class CoreAdapterConfig {
    @Resource
    EntityManager entityManager;

    @Bean
    GenericRepository<BizParameter, StringIdentity> createBizParameterRepository() {
        return new GenericRepository<>(BizParameter.class, entityManager);
    }

}
