package com.stardata.starshop2.ordercontext.command.usertype;

import com.stardata.starshop2.ordercontext.command.domain.order.OrderItemStatus;
import com.stardata.starshop2.sharedcontext.usertype.PersistentEnumUserType;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/28 14:04
 */
public class OrderTypeUserType extends PersistentEnumUserType<OrderItemStatus> {

    @Override
    public Class<OrderItemStatus> returnedClass() {
        return OrderItemStatus.class;
    }
}
