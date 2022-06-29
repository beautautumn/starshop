package com.stardata.starshop2.ordercontext.command.domain.order;

import com.stardata.starshop2.sharedcontext.usertype.PersistentCharEnum;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:26
 */
public enum OrderItemStatus implements PersistentCharEnum {
    TO_PICK('1'),
    TO_DISPATCH('2'),
    DISPATCHED('3');

    private final char value;

    OrderItemStatus(char value) {
        this.value = value;
    }

    @Override
    public char getValue() {
        return this.value;
    }


}
