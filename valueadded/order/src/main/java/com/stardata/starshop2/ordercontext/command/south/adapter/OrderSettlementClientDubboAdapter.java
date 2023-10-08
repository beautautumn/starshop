package com.stardata.starshop2.ordercontext.command.south.adapter;

import com.stardata.starshop2.api.ProductBizService;
import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import com.stardata.starshop2.ordercontext.command.south.port.OrderSettlementClient;
import com.stardata.starshop2.pl.ProductSettlementRequest;
import com.stardata.starshop2.pl.ProductSettlementResponse;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/6 11:20
 */
@Adapter(PortType.Client)
@Component("orderSettlementClientDubboAdapter")
@AllArgsConstructor
@Primary
public class OrderSettlementClientDubboAdapter implements OrderSettlementClient {
//    @DubboReference
    private final ProductBizService productBizService;

    @Override
    public void settleProducts(@NotNull Order order) {
        List<Long> productIds = new ArrayList<>();
        List<Integer> productCounts = new ArrayList<>();
        order.getItems().forEach(item -> {
            productIds.add(item.getProductId().value());
            productCounts.add(item.getPurchaseCount());
        });
        ProductSettlementRequest request = new ProductSettlementRequest(productIds, productCounts);
        ProductSettlementResponse response = productBizService.calcSettlement(request);
        for (ProductSettlementResponse.Item item : response.getItems()) {
            order.settleItem(LongIdentity.from(item.getId()), item.getName(), item.getCount(),
                    item.getPriceFen(), item.getProductSnapshot());
        }
    }
}
