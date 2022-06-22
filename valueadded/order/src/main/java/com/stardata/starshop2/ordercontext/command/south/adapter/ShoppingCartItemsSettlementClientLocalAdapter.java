package com.stardata.starshop2.ordercontext.command.south.adapter;

import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCart;
import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCartItemSubtotal;
import com.stardata.starshop2.ordercontext.command.south.port.ShoppingCartItemsSettlementClient;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import org.springframework.stereotype.Component;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/6 11:17
 */
@Adapter(PortType.Client)
@Component
public class ShoppingCartItemsSettlementClientLocalAdapter implements ShoppingCartItemsSettlementClient {
    @Override
    public void settleProducts(ShoppingCart shoppingCart) {
        //todo 完成商品上下文调用，获取商品价格统计
        ShoppingCartItemSubtotal subtotal = ShoppingCartItemSubtotal.from(0, 0, false);
        shoppingCart.getItems().forEach(item -> shoppingCart.refreshItemSubTotal(item, subtotal));
    }
}
