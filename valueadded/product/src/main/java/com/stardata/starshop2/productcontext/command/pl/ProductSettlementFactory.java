package com.stardata.starshop2.productcontext.command.pl;

import com.stardata.starshop2.api.ProductSettlementRequest;
import com.stardata.starshop2.api.ProductSettlementResponse;
import com.stardata.starshop2.productcontext.command.domain.product.ProductSettlement;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/8/18 09:02
 */
public class ProductSettlementFactory {
    public static Map<LongIdentity, Integer> composeRequestToMap(ProductSettlementRequest request) {
        List<LongIdentity> ids = request.getProductIds().stream().map(LongIdentity::from).collect(Collectors.toList());

        Map<LongIdentity, Integer> productCountsMap = new HashMap<>();
        for (int i = 0; i < request.getProductIds().size(); i++) {
            productCountsMap.put(ids.get(i), request.getProductCounts().get(i));
        }
        return productCountsMap;
    }

    public static ProductSettlementResponse settlementToResponse(Map<LongIdentity, ProductSettlement> settlements) {
        ProductSettlementResponse result = new ProductSettlementResponse();
        settlements.values().forEach(item -> result.getItems().add(ProductSettlementResponseItemMapper.INSTANCE.convert(item)));
        return result;
    }
}
