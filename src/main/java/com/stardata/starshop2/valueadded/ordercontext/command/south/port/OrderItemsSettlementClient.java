package com.stardata.starshop2.valueadded.ordercontext.command.south.port;

import com.stardata.starshop2.valueadded.ordercontext.command.domain.order.Order;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/26 18:12
 */
public interface OrderItemsSettlementClient {
    void settleItems(Order order) ;
}
