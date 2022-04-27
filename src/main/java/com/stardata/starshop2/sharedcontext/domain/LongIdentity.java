package com.stardata.starshop2.sharedcontext.domain;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/27 16:02
 */
public class LongIdentity implements Identity<Long>{
    private Long value;

    @Override
    public Long value() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
