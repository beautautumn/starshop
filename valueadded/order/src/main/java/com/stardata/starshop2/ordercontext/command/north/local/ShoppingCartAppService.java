package com.stardata.starshop2.ordercontext.command.north.local;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.pl.SessionUser;
import com.stardata.starshop2.ordercontext.command.domain.ShoppingCartManagingService;
import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCart;
import com.stardata.starshop2.ordercontext.command.pl.ShoppingCartRequest;
import com.stardata.starshop2.ordercontext.command.pl.ShoppingCartResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/26 17:50
 */
@Service
@AllArgsConstructor
public class ShoppingCartAppService {
    private final ShoppingCartManagingService shoppingCartManagingService;

    public ShoppingCartResponse save(SessionUser loginUser, Long shopId, ShoppingCartRequest request) {
        ShoppingCart shoppingCart = request.toShoppingCart(shopId, loginUser.getId());
        return ShoppingCartResponse.from(shoppingCartManagingService.replaceShoppingCart(shoppingCart));
    }

    public ShoppingCartResponse query(Long userIdLong, Long shopIdLong) {
        LongIdentity userId = LongIdentity.from(userIdLong);
        LongIdentity shopId = LongIdentity.from(shopIdLong);
        return  ShoppingCartResponse.from((shoppingCartManagingService.queryShoppingCart(userId, shopId)));
    }
}
