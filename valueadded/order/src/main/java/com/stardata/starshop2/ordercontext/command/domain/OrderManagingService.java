package com.stardata.starshop2.ordercontext.command.domain;

import com.stardata.starshop2.ordercontext.command.domain.order.*;
import com.stardata.starshop2.sharedcontext.domain.BizParameter;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.south.port.BizParameterRepository;
import com.stardata.starshop2.ordercontext.command.south.port.OrderItemsSettlementClient;
import com.stardata.starshop2.ordercontext.command.south.port.OrderRepository;
import com.stardata.starshop2.ordercontext.command.south.port.WxPrepayingClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/29 14:03
 */
@Service
@AllArgsConstructor
public class OrderManagingService {
    private final OrderItemsSettlementClient settlementClient;
    private final WxPrepayingClient prepayingClient;
    private final OrderRepository orderRepository;
    private final BizParameterRepository parameterRepository;

    public void submitOrder(LongIdentity userId, Order order) {
        settlementClient.settleProducts(order);
        order.createPayment();
        order.recordOperLog(userId, OrderOperType.CREATE, null);
        orderRepository.add(order);
    }

    public WxPrepayOrder prepayOrder(LongIdentity orderId) {
        Order order = orderRepository.instanceOf(orderId);
        return prepayingClient.prepay(order.getPayment());
    }

    public Order detail(LongIdentity orderId) {
        return orderRepository.instanceOf(orderId);
    }

    public Order makeOrderEffectively(WxPayResult wxPayResult) {
        Order order = orderRepository.findByOutTradeNo(wxPayResult.getOutTradeNo());
        order.makeEffectively();
        orderRepository.update(order);
        return order;
    }

    public Order closeOrder(LongIdentity orderId) {
        Order order = orderRepository.instanceOf(orderId);
        order.close();
        orderRepository.update(order);
        return order;
    }

    public void closeOrder(Order order) {
        order.close();
        orderRepository.update(order);
    }

    public List<Order> getConfirmExpired() {
        BizParameter parameter = parameterRepository.instanceOf("max_minutes_remain_toconfirm");
        int maxMinutes = parameter.toInteger();
        return orderRepository.findConfirmExpired(maxMinutes);
    }

    public Order setInvisible(LongIdentity orderId) {
        Order order = orderRepository.instanceOf(orderId);
        order.setInvisible();
        orderRepository.update(order);
        return order;
    }

    public List<Order> getPayExpired() {
        BizParameter parameter = parameterRepository.instanceOf("max_minutes_remain_topay");
        int maxMinutes = parameter.toInteger();
        return orderRepository.findPayExpired(maxMinutes);
    }

    public void cancelOrder(Order order) {
        order.cancel();
        orderRepository.update(order);
    }
}
