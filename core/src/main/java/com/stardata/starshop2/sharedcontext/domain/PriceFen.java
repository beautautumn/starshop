package com.stardata.starshop2.sharedcontext.domain;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/18 20:15
 */
@Embeddable
public class PriceFen {
    private Long value = null;

    public PriceFen(long value) {
        if (value >= 0) {
            this.value = value;
        } else {
            this.value = null;
        }
    }

    protected PriceFen() {
        new PriceFen(-1);
    }

    public Long value(){return this.value;}

    public PriceFen multiply(int count) {
        return new PriceFen(this.value *count);
    }

    public PriceFen multiply(NonNegativeDecimal rate) {
        return new PriceFen(BigDecimal.valueOf(this.value).multiply(rate.value()).longValue());
    }
}
