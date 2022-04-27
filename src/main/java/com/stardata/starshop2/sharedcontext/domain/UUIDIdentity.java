package com.stardata.starshop2.sharedcontext.domain;

import javax.annotation.concurrent.Immutable;
import java.util.UUID;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/24 18:40
 */
@Immutable
public class UUIDIdentity implements RandomIdentity<String>{
    private String value;
    private static final long serialVersionUID = 1L;

    public UUIDIdentity() {
        this.value = next();
    }

    @Override
    public String next() {
        this.value = UUID.randomUUID().toString();
        return this.value;
    }

    @Override
    public String value() {
        return this.value;
    }
}
