package com.stardata.starshop2.ordercontext.command.south.port;

import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCart;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/26 18:12
 */
public interface ShoppingCartItemsSettlementClient {
    void settleProducts(ShoppingCart shoppingCart) ;
}