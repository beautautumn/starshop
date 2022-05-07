package com.stardata.starshop2.ordercontext.command.south.port;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.ordercontext.command.domain.order.Order;

import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/29 14:39
 */
public interface OrderRepository {
    void add(Order order);

    Order instanceOf(LongIdentity orderId);

    Order findByOutTradeNo(String outTradeNo);

    void update(Order order);

    List<Order> findConfirmExpired(int maxMinutes);

    List<Order> findPayExpired(int maxMinutes);
}
