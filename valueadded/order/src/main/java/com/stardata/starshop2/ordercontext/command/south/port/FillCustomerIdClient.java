package com.stardata.starshop2.ordercontext.command.south.port;

import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import org.jetbrains.annotations.NotNull;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/7 19:26
 */
public interface FillCustomerIdClient {
    void fillCustomerId(@NotNull Order order);
}
