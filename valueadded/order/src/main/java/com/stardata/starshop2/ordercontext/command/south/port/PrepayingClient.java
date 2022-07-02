package com.stardata.starshop2.ordercontext.command.south.port;

import com.stardata.starshop2.ordercontext.command.domain.order.OrderPayment;
import com.stardata.starshop2.ordercontext.command.domain.order.PrepayOrder;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/29 16:14
 */
public interface PrepayingClient {
    PrepayOrder prepay(OrderPayment payment);
}
