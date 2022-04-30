package com.stardata.starshop2.sharedcontext.domain;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/29 14:18
 */
public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
