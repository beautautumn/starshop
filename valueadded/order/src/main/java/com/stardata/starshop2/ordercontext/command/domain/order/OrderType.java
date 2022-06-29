package com.stardata.starshop2.ordercontext.command.domain.order;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/28 14:23
 */
public enum OrderType {
    SHOP('1'),
    SHOP_SUB('1');

    private final Character value;

    OrderType(Character value) {
        this.value = value;
    }

    public Character toCharacter() { return this.value; }
}
