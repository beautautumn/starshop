package com.stardata.starshop2.ordercontext.command.south.port;

import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import com.stardata.starshop2.ordercontext.command.domain.order.OrderPayment;
import com.stardata.starshop2.ordercontext.command.domain.order.WxPrepayOrder;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/29 16:14
 */
public interface WxPrepayingClient {
    WxPrepayOrder prepay(OrderPayment payment);
}
