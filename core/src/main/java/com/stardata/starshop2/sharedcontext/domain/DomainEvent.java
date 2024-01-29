package com.stardata.starshop2.sharedcontext.domain;

import com.stardata.starshop2.sharedcontext.exception.ApplicationValidationException;
import com.stardata.starshop2.sharedcontext.helper.JSONUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/29 14:13
 */
@Getter
public class DomainEvent implements Serializable{
    protected final Long unixTimestamp;
    protected final String topic;
    protected final String id;
    protected final String name;
    protected final Serializable body;

    public DomainEvent(@NotNull String topic, @NotNull String name, @NotNull Serializable body) {
        if (StringUtils.isBlank(topic) || StringUtils.isBlank(name)) {
            throw new ApplicationValidationException("DomainEvent's domain and eventName must not be blank.");
        }
        this.topic = topic;
        this.name = name;
        this.body = body;
        this.id = UUID.randomUUID().toString();
        this.unixTimestamp =System.currentTimeMillis()/1000;
    }

    public String toString(){
        return JSONUtil.toJSONString(this);
    }
}
