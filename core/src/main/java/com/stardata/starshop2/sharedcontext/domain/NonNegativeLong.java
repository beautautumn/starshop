package com.stardata.starshop2.sharedcontext.domain;

import com.stardata.starshop2.sharedcontext.exception.DomainException;
import javax.persistence.Embeddable;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/18 20:15
 */
@Embeddable
public class NonNegativeLong {
    private final Long value;


    NonNegativeLong(long value) {
        if (value < 0L) {
            throw new DomainException("NonNegativeLong value can not be negative.");
        }
        this.value = value;
    }

    protected NonNegativeLong() {
        this(0);
    }

    public Long value() {return this.value;}
}
