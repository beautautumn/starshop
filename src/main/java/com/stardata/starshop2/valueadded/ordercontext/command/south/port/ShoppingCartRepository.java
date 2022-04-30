package com.stardata.starshop2.valueadded.ordercontext.command.south.port;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.valueadded.ordercontext.command.domain.shoppingcart.ShoppingCart;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/27 18:57
 */
public interface ShoppingCartRepository {
    void update(ShoppingCart shoppingCartWithSettlement);

    ShoppingCart findForUserInShop(LongIdentity userId, LongIdentity shopId);
}
