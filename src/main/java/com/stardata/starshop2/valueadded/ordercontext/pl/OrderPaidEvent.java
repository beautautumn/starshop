package com.stardata.starshop2.valueadded.ordercontext.pl;

import com.stardata.starshop2.sharedcontext.domain.DomainEvent;
import com.stardata.starshop2.valueadded.ordercontext.command.domain.order.Order;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

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
