package com.stardata.starshop2.ordercontext.command.north.local;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.stardata.starshop2.ordercontext.command.domain.OrderManagingService;
import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import com.stardata.starshop2.ordercontext.command.domain.order.WxPayResult;
import com.stardata.starshop2.ordercontext.command.domain.order.WxPrepayOrder;
import com.stardata.starshop2.ordercontext.command.pl.*;
import com.stardata.starshop2.ordercontext.command.south.port.OrderEventPublisher;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.pl.SessionUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/29 13:54
 */
@Service
@AllArgsConstructor
public class OrderAppService {
    private final OrderManagingService managingService;
    private final OrderEventPublisher orderEventPublisher;

    public OrderResponse create(SessionUser loginUser, Long shopId, OrderSubmitRequest request) {
        Order order = request.toOrder(loginUser.getId(), shopId);
        managingService.submitOrder(LongIdentity.from(loginUser.getId()), order);
        return OrderResponse.from(order);
    }

    public PrepayResponse prepay(Long orderIdLong) {
        LongIdentity orderId = LongIdentity.from(orderIdLong);
        WxPrepayOrder prepay = managingService.prepayOrder(orderId);
        return PrepayResponse.from(prepay);
    }

    public OrderResponse getDetail(Long orderIdLong) {
        LongIdentity orderId = LongIdentity.from(orderIdLong);
        Order order = managingService.detail(orderId);
        return OrderResponse.from(order);
    }

    public String handleWxPayNotify(OrderPayResultRequest request) {
        try {
            WxPayResult wxPayResult = request.toWxPayResult();
            Order order = managingService.makeOrderEffectively(wxPayResult);

            OrderPaidEvent orderEvent = new OrderPaidEvent(order);
            orderEventPublisher.publish(orderEvent);
            return WxPayNotifyResponse.success("成功");
        } catch (Exception e) {
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }

    public OrderConfirmedResponse confirmReceived(Long orderIdLong) {
        LongIdentity orderId = LongIdentity.from(orderIdLong);
        Order order = managingService.closeOrder(orderId);
        OrderClosedEvent orderEvent = new OrderClosedEvent(order);
        orderEventPublisher.publish(orderEvent);
        return OrderConfirmedResponse.from(order);
    }

    public void autoConfirm() {
        List<Order> orders = managingService.getConfirmExpired();
        for (Order order : orders) {
            managingService.closeOrder(order);
            OrderClosedEvent orderEvent = new OrderClosedEvent(order);
            orderEventPublisher.publish(orderEvent);
        }
    }

    public OrderDeletedResponse delete(Long orderIdLong) {
        LongIdentity orderId = LongIdentity.from(orderIdLong);
        Order order = managingService.setInvisible(orderId);
        return OrderDeletedResponse.from(order);
    }

    public void autoCancel() {
        List<Order> orders = managingService.getPayExpired();
        for (Order order : orders) {
            managingService.cancelOrder(order);
        }
    }
}