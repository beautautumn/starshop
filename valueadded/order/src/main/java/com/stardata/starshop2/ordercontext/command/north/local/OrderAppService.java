package com.stardata.starshop2.ordercontext.command.north.local;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.stardata.starshop2.ordercontext.command.domain.OrderManagingService;
import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import com.stardata.starshop2.ordercontext.command.domain.order.PrepayOrder;
import com.stardata.starshop2.ordercontext.command.pl.*;
import com.stardata.starshop2.ordercontext.command.south.port.OrderEventPublisher;
import com.stardata.starshop2.ordercontext.command.south.port.OrderRepository;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.domain.SessionUser;
import com.stardata.starshop2.sharedcontext.pl.EventConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final OrderRepository orderRepository;


    @Transactional
    public OrderResponse create(SessionUser loginUser, Long shopId, OrderSubmitRequest request) {
        Order order = request.toOrder(LongIdentity.from(shopId), loginUser.getId());
        managingService.submitOrder(loginUser, order);
        return OrderResponse.from(order);
    }

    @Transactional
    public PrepayOrderResponse prepay(Long orderIdLong) {
        LongIdentity orderId = LongIdentity.from(orderIdLong);
        PrepayOrder prepay = managingService.prepayOrder(orderId);
        return PrepayOrderResponse.from(prepay);
    }

    public OrderResponse getDetail(Long orderIdLong) {
        LongIdentity orderId = LongIdentity.from(orderIdLong);
        Order order = orderRepository.instanceOf(orderId);
        return OrderResponse.from(order);
    }

    @Transactional
    public String handleWxPayNotify(OrderPayResultRequest request) {
        try {
            Order order = managingService.makeOrderEffectively(request.toPayResult());
            orderEventPublisher.publish(new OrderEvent(EventConstants.EventOperator.PAY,order));
            return WxPayNotifyResponse.success("success");
        } catch (Exception e) {
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }

    @Transactional
    public OrderConfirmedResponse confirmReceived(Long orderIdLong) {
        LongIdentity orderId = LongIdentity.from(orderIdLong);
        Order order = managingService.closeOrder(orderId);
        orderEventPublisher.publish(new OrderEvent(EventConstants.EventOperator.CLOSE,order));
        return OrderConfirmedResponse.from(order);
    }

    @Transactional
    public void autoConfirm() {
        List<Order> orders = managingService.getConfirmExpired();
        for (Order order : orders) {
            managingService.closeOrder(order);
            orderEventPublisher.publish(new OrderEvent(EventConstants.EventOperator.CLOSE,order));
        }
    }

    @Transactional
    public OrderDeletedResponse delete(Long orderIdLong) {
        LongIdentity orderId = LongIdentity.from(orderIdLong);
        Order order = managingService.setInvisible(orderId);
        return OrderDeletedResponse.from(order);
    }

    @Transactional
    public void autoCancel() {
        List<Order> orders = managingService.getPayExpired();
        for (Order order : orders) {
            managingService.cancelOrder(order);
            orderEventPublisher.publish(new OrderEvent(EventConstants.EventOperator.CANCEL,order));
        }
    }
}
