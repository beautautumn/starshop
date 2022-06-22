package com.stardata.starshop2.ordercontext.command.south.adapter;

import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import com.stardata.starshop2.ordercontext.command.south.port.OrderItemsSettlementClient;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import org.springframework.stereotype.Component;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/6 11:20
 */
@Adapter(PortType.Client)
@Component
public class OrderItemsSettlementClientLocalAdapter implements OrderItemsSettlementClient {
    @Override
    public void settleItems(Order order) {

    }
}
