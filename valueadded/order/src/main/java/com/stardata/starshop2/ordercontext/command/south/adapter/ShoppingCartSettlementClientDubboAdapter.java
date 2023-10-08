package com.stardata.starshop2.ordercontext.command.south.adapter;

import com.stardata.starshop2.api.ProductBizService;
import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCart;
import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCartItemSubtotal;
import com.stardata.starshop2.ordercontext.command.south.port.ShoppingCartSettlementClient;
import com.stardata.starshop2.pl.ProductSettlementRequest;
import com.stardata.starshop2.pl.ProductSettlementResponse;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.domain.NonNegativeDecimal;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
@Component("shoppingCartSettlementClientDubboAdapter")
@AllArgsConstructor
@Primary
public class ShoppingCartSettlementClientDubboAdapter implements ShoppingCartSettlementClient {
//    @DubboReference
    private final ProductBizService productBizService;

    @Override
    public void settleProducts(@NotNull ShoppingCart shoppingCart) {
        List<Long> productIds = new ArrayList<>();
        List<Integer> productCounts = new ArrayList<>();
        shoppingCart.getItems().forEach(item -> {
            productIds.add(item.getProductId().value());
            productCounts.add(item.getCount());
        });
        ProductSettlementRequest request = new ProductSettlementRequest(productIds, productCounts);
        ProductSettlementResponse response = productBizService.calcSettlement(request);

        Map<LongIdentity, ShoppingCartItemSubtotal> itemsSubtotal = new HashMap<>();
        for (ProductSettlementResponse.Item item : response.getItems()) {
            ShoppingCartItemSubtotal subtotal = new ShoppingCartItemSubtotal(
                    item.getCount(),
                    item.getPriceFen(),
                    new NonNegativeDecimal(item.getQuantity()),
                    item.isAvailable());
            itemsSubtotal.put(LongIdentity.from(item.getId()), subtotal);
        }
        shoppingCart.refreshItemsSubTotal(itemsSubtotal);
    }
}
