package com.stardata.starshop2.ordercontext.command.domain.order;

import com.stardata.starshop2.sharedcontext.usertype.PersistentCharEnum;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/28 14:23
 */
public enum OrderType implements PersistentCharEnum {
    SHOP('1'),
    SHOP_SUB('2');

    private final char value;

    OrderType(char value) {
        this.value = value;
    }

    @Override
    public char value() { return this.value; }
}
