package com.stardata.starshop2.ordercontext.command.usertype;

import com.stardata.starshop2.ordercontext.command.domain.order.OrderOperType;
import com.stardata.starshop2.sharedcontext.usertype.PersistentEnumUserType;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/28 14:04
 */
public class OrderOperTypeUserType extends PersistentEnumUserType<OrderOperType> {

    @Override
    public Class<OrderOperType> returnedClass() {
        return OrderOperType.class;
    }
}
