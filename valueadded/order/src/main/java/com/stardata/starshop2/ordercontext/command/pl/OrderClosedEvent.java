package com.stardata.starshop2.ordercontext.command.pl;

import com.stardata.starshop2.sharedcontext.domain.DomainEvent;
import com.stardata.starshop2.ordercontext.command.domain.order.Order;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/30 20:37
 */
public class OrderClosedEvent extends DomainEvent {
    public OrderClosedEvent(Order order) {
        //todo 完成根据订单创建"订单已关闭"的领域事件对象
    }
}
