package com.stardata.starshop2.authcontext.south.adapter;

import com.stardata.starshop2.authcontext.domain.loginlog.LoginLog;
import com.stardata.starshop2.authcontext.domain.user.User;
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
public class AuthAdapterConfig {
    @Resource
    EntityManager entityManager;

    @Bean
    GenericRepository<User, LongIdentity> createUserRepository() {
        return new GenericRepository<>(User.class, entityManager);
    }

    @Bean
    GenericRepository<LoginLog, LongIdentity> createLoginLogRepository() {
        return new GenericRepository<>(LoginLog.class, entityManager);
    }
}
