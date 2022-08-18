package com.stardata.starshop2.ordercontext.command.domain;

import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import com.stardata.starshop2.ordercontext.command.domain.order.OrderOperType;
import com.stardata.starshop2.ordercontext.command.domain.order.PayResult;
import com.stardata.starshop2.ordercontext.command.domain.order.PrepayOrder;
import com.stardata.starshop2.ordercontext.command.south.port.OrderItemsSettlementClient;
import com.stardata.starshop2.ordercontext.command.south.port.OrderRepository;
import com.stardata.starshop2.ordercontext.command.south.port.PrepayingClient;
import com.stardata.starshop2.sharedcontext.domain.BizParameter;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.domain.SessionUser;
import com.stardata.starshop2.sharedcontext.south.port.BizParameterRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    @Resource(name="${adapter.orderItemsSettlement}")
    private OrderItemsSettlementClient settlementClient;
    private final PrepayingClient prepayingClient;
    private final OrderRepository orderRepository;
    private final BizParameterRepository parameterRepository;

    private PrepayRequestGenerationService prepayRequestGenerationService;

    public void submitOrder(@NotNull SessionUser user, @NotNull Order order) {
        settlementClient.settleProducts(order);
        String requestMessage = prepayRequestGenerationService.generatePrepay(user, order);
        order.createPayment(requestMessage);
        order.recordOperLog(user.getId(), OrderOperType.CREATE, null);
        orderRepository.add(order);
    }

    public PrepayOrder prepayOrder(LongIdentity orderId) {
        Order order = orderRepository.instanceOf(orderId);
        PrepayOrder result = prepayingClient.prepay(order.getPayment());
        orderRepository.update(order);
        return result;
    }

    public Order detail(LongIdentity orderId) {
        return orderRepository.instanceOf(orderId);
    }

    public Order makeOrderEffectively(PayResult payResult) {
        Order order = orderRepository.findByOutTradeNo(payResult.getOutTradeNo());
        order.makeEffective(payResult);
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
        //如果数据库未配置参数，则默认48小时确认订单
        int maxMinutes = parameter.toInteger(48*60);
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
        //如果数据库未配置参数，则默认30分钟支付订单
        int maxMinutes = parameter.toInteger(30);
        return orderRepository.findPayExpired(maxMinutes);
    }

    public void cancelOrder(Order order) {
        order.cancel();
        orderRepository.update(order);
    }
}
