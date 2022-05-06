package com.stardata.starshop2.valueadded.productcontext.command.north.local;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.valueadded.productcontext.command.domain.ProductManagingService;
import com.stardata.starshop2.valueadded.productcontext.command.domain.ProductSettlementService;
import com.stardata.starshop2.valueadded.productcontext.command.domain.product.Product;
import com.stardata.starshop2.valueadded.productcontext.command.domain.product.ProductSettlement;
import com.stardata.starshop2.valueadded.productcontext.pl.ProductResponse;
import com.stardata.starshop2.valueadded.productcontext.pl.ProductSettlementRequest;
import com.stardata.starshop2.valueadded.productcontext.pl.ProductSettlementResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<ProductSettlement> settlements = new ArrayList<>();
        int totalPriceFen = settlementService.calcSettlement(request.composeToMap(), settlements);
        return ProductSettlementResponse.from(settlements, totalPriceFen);
    }

    public void increaseCurMonthSale(ProductSettlementRequest request) {
        settlementService.increaseCurMonthSale(request.composeToMap());
    }

    public ProductResponse getDetail(Long productIdLong) {
        LongIdentity productId = LongIdentity.from(productIdLong);
        Product product = managingService.detail(productId);
        return ProductResponse.from(product);

    }
}
