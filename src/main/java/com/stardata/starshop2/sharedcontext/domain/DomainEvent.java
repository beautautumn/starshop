package com.stardata.starshop2.sharedcontext.domain;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/29 14:13
 */
@Data
public abstract class DomainEvent {
    protected final String eventId;
    protected final String occurredOn;

    public DomainEvent() {
        eventId = UUID.randomUUID().toString();
        occurredOn = new Timestamp(System.currentTimeMillis()).toString();
    }
}
