package com.stardata.starshop2.ordercontext.command.pl;

import com.stardata.starshop2.sharedcontext.domain.DomainEvent;
import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import lombok.Getter;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/29 14:19
 */
@Getter
public class OrderPaidEvent extends DomainEvent {

    public OrderPaidEvent(Order order) {
        //todo 完成根据订单创建"订单已付款"的领域事件对象
    }
}
