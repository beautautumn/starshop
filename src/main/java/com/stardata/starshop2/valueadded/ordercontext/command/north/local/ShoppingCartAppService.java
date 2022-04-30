package com.stardata.starshop2.valueadded.ordercontext.command.north.local;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.pl.SessionUser;
import com.stardata.starshop2.valueadded.ordercontext.command.domain.ShoppingCartManagingService;
import com.stardata.starshop2.valueadded.ordercontext.command.domain.shoppingcart.ShoppingCart;
import com.stardata.starshop2.valueadded.ordercontext.pl.ShoppingCartRequest;
import com.stardata.starshop2.valueadded.ordercontext.pl.ShoppingCartResponse;
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

    public ShoppingCartResponse save(SessionUser loginUser, String shopId, ShoppingCartRequest request) {
        ShoppingCart shoppingCart = request.toShoppingCart(shopId, loginUser.getId());
        return ShoppingCartResponse.from(shoppingCartManagingService.replaceShoppingCart(shoppingCart));
    }

    public ShoppingCartResponse query(String userIdStr, String shopIdStr) {
        LongIdentity userId = LongIdentity.from(userIdStr);
        LongIdentity shopId = LongIdentity.from(shopIdStr);
        return  ShoppingCartResponse.from((shoppingCartManagingService.queryShoppingCart(userId, shopId)));
    }
}
