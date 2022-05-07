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
public class ShoppingCartRequest {
    public ShoppingCart toShoppingCart(String shopId, String userId) {
        //todo 完成根据DTO创建购物车的工厂方法
        return null;
    }
}
