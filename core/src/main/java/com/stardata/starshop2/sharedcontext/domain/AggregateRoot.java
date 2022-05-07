package com.stardata.starshop2.sharedcontext.domain;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/24 18:24
 */
public interface AggregateRoot<R extends AbstractEntity> {
    R root();
}
