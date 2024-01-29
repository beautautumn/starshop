package com.stardata.starshop2.ordercontext.command.pl;

import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import com.stardata.starshop2.sharedcontext.domain.DomainEvent;
import com.stardata.starshop2.sharedcontext.pl.EventConstants;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2024/1/29 18:22
 */
@Getter
public class OrderEvent extends DomainEvent {
    public OrderEvent(@NotNull String operation, @NotNull Order order) {
        super(EventConstants.EventTopic.ORDER_EVENT, operation, OrderResponse.from(order));
    }
}
