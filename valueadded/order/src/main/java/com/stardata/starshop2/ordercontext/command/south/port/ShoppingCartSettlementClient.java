package com.stardata.starshop2.ordercontext.command.south.port;

import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCart;
import org.jetbrains.annotations.NotNull;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/26 18:12
 */
public interface ShoppingCartSettlementClient {
    void settleProducts(@NotNull ShoppingCart shoppingCart) ;
}
