package com.stardata.starshop2.ordercontext.command.pl;

import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCart;
import lombok.Data;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/26 18:04
 */
@Data
public class ShoppingCartResponse {
    public static ShoppingCartResponse from(ShoppingCart saveShoppingCart) {
        //todo 完成购物车对象转DTO
        return null;
    }
}
