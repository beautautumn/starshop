package com.stardata.starshop2.ordercontext.command.domain.order;

import com.stardata.starshop2.sharedcontext.exception.ApplicationValidationException;
import com.stardata.starshop2.sharedcontext.usertype.PersistentCharEnum;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:29
 */
public enum OrderVisibility implements PersistentCharEnum {
    INVISIBLE('0'),
    VISIBLE('1');

    private final Character value;

    OrderVisibility(char value) {
        this.value = value;
    }

    @Override
    public char value() { return this.value; }

    public static OrderVisibility of(Character value) {
        for (OrderVisibility item : OrderVisibility.values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        throw new ApplicationValidationException("Invalid RecordVisibility value "+value);
    }

    public static OrderVisibility of(Integer value) {
        for (OrderVisibility item : OrderVisibility.values()) {
            if (item.ordinal() == value) {
                return item;
            }
        }
        throw new ApplicationValidationException("Invalid RecordVisibility value "+value);
    }
}

