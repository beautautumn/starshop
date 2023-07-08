package com.stardata.starshop2.ordercontext.command.domain.order;

import com.stardata.starshop2.sharedcontext.usertype.PersistentCharEnum;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:26
 */
public enum PaymentStatus implements PersistentCharEnum {
    TO_PAY('1'),
    SUCCESS('2'),
    FAILED('3');
    private final char value;

    PaymentStatus(char value) {
        this.value = value;
    }

    @Override
    public char value() { return this.value; }

}
