package com.stardata.starshop2.productcontext.command.domain.product;

import com.stardata.starshop2.sharedcontext.usertype.PersistentCharEnum;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/28 14:16
 */
public enum ProductDiscountType implements PersistentCharEnum {
    NONE('0'), BY_RATE('1'), FIXED_PRICE('2');

    private final Character value;

    ProductDiscountType(Character value) {
        this.value = value;
    }

    @Override
    public char getValue() {
        return this.value;
    }
}
