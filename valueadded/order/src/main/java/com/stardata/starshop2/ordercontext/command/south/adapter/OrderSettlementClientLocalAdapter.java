package com.stardata.starshop2.ordercontext.command.south.adapter;

import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import com.stardata.starshop2.ordercontext.command.south.port.OrderSettlementClient;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/6 11:20
 */
@Adapter(PortType.Client)
@Component("orderSettlementClientLocalAdapter")
@AllArgsConstructor
public class OrderSettlementClientLocalAdapter implements OrderSettlementClient {
//    private final ProductAppService productAppService;

    @Override
    public void settleProducts(@NotNull Order order) {
//        List<Long> productIds = new ArrayList<>();
//        List<Integer> productCounts = new ArrayList<>();
//        order.getItems().forEach(item -> {
//            productIds.add(item.getProductId().value());
//            productCounts.add(item.getPurchaseCount());
//        });
//        ProductSettlementRequest request = new ProductSettlementRequest(productIds, productCounts);
//        ProductSettlementResponse response = productAppService.calcSettlement(request);
//        for (ProductSettlementResponse.Item item : response.getItems()) {
//            order.settleItem(LongIdentity.from(item.getId()), item.getName(), item.getCount(),
//                    item.getPriceFen(), item.getProductSnapshot());
//        }
    }
}
