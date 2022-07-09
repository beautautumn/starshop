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
public class NonNegativeInteger {
    private final Integer value;


    public NonNegativeInteger(Integer value) {
        if (value < 0) {
            throw new DomainException("NonNegativeInteger value can not be negative.");
        }
        this.value = value;
    }

    public  NonNegativeInteger() {
        this(0);
    }

    public Integer value() {return this.value;}

}
