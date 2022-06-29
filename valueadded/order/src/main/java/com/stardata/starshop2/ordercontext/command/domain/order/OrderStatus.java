package com.stardata.starshop2.ordercontext.command.domain.order;

import com.stardata.starshop2.sharedcontext.usertype.PersistentCharEnum;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:26
 */
public enum OrderStatus implements PersistentCharEnum {
    TO_PAY('1'),
    PAID('2'),
    PICKED('3'),
    DISPATCHED('4'),
    CLOSED('5'),
    CANCELLED('6');

    private final char value;

    OrderStatus(char value) {
        this.value = value;
    }

    @Override
    public char getValue() { return this.value; }
}
