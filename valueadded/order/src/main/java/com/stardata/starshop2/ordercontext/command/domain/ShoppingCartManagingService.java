package com.stardata.starshop2.ordercontext.command.domain;

import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCart;
import com.stardata.starshop2.ordercontext.command.south.port.ShoppingCartItemsSettlementClient;
import com.stardata.starshop2.ordercontext.command.south.port.ShoppingCartRepository;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/26 18:07
 */
@Service
@AllArgsConstructor
public class ShoppingCartManagingService {
    private final ShoppingCartItemsSettlementClient settlementClient;
    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCart replaceShoppingCart(@NotNull ShoppingCart shoppingCart) {
        settlementClient.settleProducts(shoppingCart);
        shoppingCartRepository.update(shoppingCart);
        return shoppingCart;
    }

    public ShoppingCart queryShoppingCart(@NotNull LongIdentity userId, @NotNull LongIdentity shopId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findForUserInShop(userId, shopId);
        settlementClient.settleProducts(shoppingCart);
        return shoppingCart;
    }
}
