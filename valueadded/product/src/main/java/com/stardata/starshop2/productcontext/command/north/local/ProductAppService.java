package com.stardata.starshop2.productcontext.command.north.local;

import com.stardata.starshop2.api.ProductSettlementRequest;
import com.stardata.starshop2.api.ProductSettlementResponse;
import com.stardata.starshop2.productcontext.command.domain.ProductManagingService;
import com.stardata.starshop2.productcontext.command.domain.ProductSettlementService;
import com.stardata.starshop2.productcontext.command.domain.product.Product;
import com.stardata.starshop2.productcontext.command.domain.product.ProductSettlement;
import com.stardata.starshop2.productcontext.command.pl.ProductResponse;
import com.stardata.starshop2.productcontext.command.pl.ProductSettlementFactory;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/2/28 10:30
 */
@Service
@AllArgsConstructor
public class ProductAppService {
    private final ProductSettlementService settlementService;
    private final ProductManagingService managingService;

    public void initForShop(String shopId) {

    }

    public ProductSettlementResponse calcSettlement(ProductSettlementRequest request) {
        Map<LongIdentity, ProductSettlement> settlements = settlementService
                .calcSettlement(ProductSettlementFactory.composeRequestToMap(request));
        return ProductSettlementFactory.settlementToResponse(settlements);
    }

    public void increaseCurMonthSale(ProductSettlementRequest request) {
        settlementService.increaseCurMonthSale(ProductSettlementFactory.composeRequestToMap(request));
    }

    public ProductResponse getDetail(Long productIdLong) {
        LongIdentity productId = LongIdentity.from(productIdLong);
        Product product = managingService.detail(productId);
        return ProductResponse.from(product);

    }
}
