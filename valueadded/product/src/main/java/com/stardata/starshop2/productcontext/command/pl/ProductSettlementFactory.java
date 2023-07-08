package com.stardata.starshop2.productcontext.command.pl;

import com.stardata.starshop2.pl.ProductSettlementResponse;
import com.stardata.starshop2.productcontext.command.domain.product.ProductSettlement;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;

import java.util.Map;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/8/18 09:02
 */
public class ProductSettlementFactory {

    public static ProductSettlementResponse settlementToResponse(Map<LongIdentity, ProductSettlement> settlements) {
        ProductSettlementResponse result = new ProductSettlementResponse();
        settlements.values().forEach(item -> result.getItems().add(ProductSettlementResponseItemMapper.INSTANCE.convert(item)));
        return result;
    }
}
