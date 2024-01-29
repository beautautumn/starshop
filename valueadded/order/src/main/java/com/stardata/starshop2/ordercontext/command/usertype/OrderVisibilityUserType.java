package com.stardata.starshop2.ordercontext.command.usertype;

import com.stardata.starshop2.ordercontext.command.domain.order.OrderVisibility;
import com.stardata.starshop2.sharedcontext.usertype.PersistentEnumUserType;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/8 19:37
 */
public class OrderVisibilityUserType extends PersistentEnumUserType<OrderVisibility> {

    @Override
    public Class<OrderVisibility> returnedClass() {
        return OrderVisibility.class;
    }
}
