package com.stardata.starshop2.ordercontext.command.domain.order;

import com.stardata.starshop2.sharedcontext.usertype.PersistentCharEnum;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:29
 */
public enum DeliveryMethod implements PersistentCharEnum {
    SELF_PICK_UP('1'),
    HOME_DELIVERY('2');

    private char value;

    DeliveryMethod(char value){
        this.value = value;
    }

    @Override
    public char value() {
        return 0;
    }
}
