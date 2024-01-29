package com.stardata.starshop2.ordercontext.command.domain.order;

import com.stardata.starshop2.sharedcontext.exception.ApplicationValidationException;
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

    private final Character value;

    OrderStatus(char value) {
        this.value = value;
    }

    @Override
    public char value() { return this.value; }

    public static OrderStatus of(Character value) {
        for (OrderStatus item : OrderStatus.values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        throw new ApplicationValidationException("Invalid OrderStatus value "+value);
    }

    public static OrderStatus of(Integer value) {
        for (OrderStatus item : OrderStatus.values()) {
            if (item.ordinal() == value) {
                return item;
            }
        }
        throw new ApplicationValidationException("Invalid OrderStatus value "+value);
    }

}
