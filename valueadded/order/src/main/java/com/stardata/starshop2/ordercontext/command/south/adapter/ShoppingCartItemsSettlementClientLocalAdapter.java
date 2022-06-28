package com.stardata.starshop2.ordercontext.command.south.adapter;

import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCart;
import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCartItemSubtotal;
import com.stardata.starshop2.ordercontext.command.south.port.ShoppingCartItemsSettlementClient;
import com.stardata.starshop2.productcontext.command.domain.ProductSettlement;
import com.stardata.starshop2.productcontext.command.domain.ProductSettlementService;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.domain.NonNegativeDecimal;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/6 11:17
 */
@Adapter(PortType.Client)
@Component
@AllArgsConstructor
public class ShoppingCartItemsSettlementClientLocalAdapter implements ShoppingCartItemsSettlementClient {
    private final ProductSettlementService productSettlementService;

    @Override
    public void settleProducts(@NotNull ShoppingCart shoppingCart) {
        Map<LongIdentity, Integer> productCountsMap = new HashMap<>();
        shoppingCart.getItems().forEach(item -> productCountsMap.put(item.getProductId(), item.getCount()));
        List<ProductSettlement> settlements = productSettlementService.calcSettlement(productCountsMap);
        Map<LongIdentity, ShoppingCartItemSubtotal> itemsSubtotal = new HashMap<>();
        for (ProductSettlement settlement : settlements) {
            ShoppingCartItemSubtotal subtotal = new ShoppingCartItemSubtotal(
                    settlement.orderCount(),
                    settlement.settlePriceFen(),
                    new NonNegativeDecimal(settlement.settleQuantity()),
                    settlement.available());
            itemsSubtotal.put(settlement.productId(), subtotal);
        }
        shoppingCart.refreshItemsSubTotal(itemsSubtotal);
    }
}
