package com.stardata.starshop2.sharedcontext.domain;

import com.stardata.starshop2.sharedcontext.exception.DomainException;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/18 20:15
 */
@Embeddable
public class NonNegativeDecimal {
    private final BigDecimal value;


    public NonNegativeDecimal(BigDecimal value) {
        if (value.add(BigDecimal.ONE).longValue() < 1L) {
            throw new DomainException("NonNegativeDecimal value can not be negative.");
        }
        this.value = value;
    }

    public NonNegativeDecimal(String value) {
        BigDecimal decimal = new BigDecimal(value);
        if (decimal.add(BigDecimal.ONE).longValue() < 1L) {
            throw new DomainException("NonNegativeDecimal value can not be negative.");
        }
        this.value = decimal;
    }

    public  NonNegativeDecimal() {
        this("0.0");
    }

    public BigDecimal value() {return this.value;}

    public NonNegativeDecimal multiply(int multiple) {
        if (multiple >= 0) {
            BigDecimal decimal = new BigDecimal(multiple);
            return new NonNegativeDecimal(this.value.multiply(decimal));
        } else {
            throw new DomainException("NonNegativeDecimal must multiply by not negative.");
        }
    }

    public NonNegativeDecimal multiply(long multiple) {
        if (multiple >= 0) {
            BigDecimal decimal = new BigDecimal(multiple);
            return new NonNegativeDecimal(this.value.multiply(decimal));
        } else {
            throw new DomainException("NonNegativeDecimal must multiply by not negative.");
        }
    }

    public NonNegativeDecimal multiply(NonNegativeDecimal decimal) {
        return new NonNegativeDecimal(this.value.multiply(decimal.value));
    }
}
