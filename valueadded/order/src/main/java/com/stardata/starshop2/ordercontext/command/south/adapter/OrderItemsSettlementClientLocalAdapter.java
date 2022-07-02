package com.stardata.starshop2.ordercontext.command.south.adapter;

import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import com.stardata.starshop2.ordercontext.command.south.port.OrderItemsSettlementClient;
import com.stardata.starshop2.productcontext.command.domain.productcategory.ProductSettlement;
import com.stardata.starshop2.productcontext.command.domain.ProductSettlementService;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
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
 * @date 2022/6/6 11:20
 */
@Adapter(PortType.Client)
@Component
@AllArgsConstructor
public class OrderItemsSettlementClientLocalAdapter implements OrderItemsSettlementClient {
    private final ProductSettlementService productSettlementService;

    @Override
    public void settleProducts(@NotNull Order order) {
        Map<LongIdentity, Integer> productCountsMap = new HashMap<>();
        order.getItems().forEach(item -> productCountsMap.put(item.getProductId(), item.getPurchaseCount()));
        List<ProductSettlement> settlements = productSettlementService.calcSettlement(productCountsMap);
        for (ProductSettlement settlement : settlements) {
            order.settleItem(settlement.productId(), settlement.productName(), settlement.orderCount(),
                    settlement.settlePriceFen(), settlement.productSnapshot());
        }

    }
}
