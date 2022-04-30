package com.stardata.starshop2.sharedcontext.domain;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/27 16:02
 */
public class LongIdentity implements Identity<Long>{
    private Long value;

    LongIdentity(long id) {
        this.value = id;
    }

    @Override
    public Long value() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public static LongIdentity from(String id) {
        return new LongIdentity(Long.parseLong(id));
    }

    public static LongIdentity from(Long id) {
        return new LongIdentity(id);
    }
}
