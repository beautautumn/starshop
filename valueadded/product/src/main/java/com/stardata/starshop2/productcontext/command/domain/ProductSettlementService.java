package com.stardata.starshop2.productcontext.command.domain;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.productcontext.command.domain.product.Product;
import com.stardata.starshop2.productcontext.command.domain.product.ProductSettlement;
import com.stardata.starshop2.productcontext.command.south.port.ProductRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/4 11:55
 */
@Service
@AllArgsConstructor
public class ProductSettlementService {
    private  final ProductRepository repository;

    public int calcSettlement(@NotNull Map<LongIdentity, Integer> productCountsMap,
                              @NotNull List<ProductSettlement> settlements) {
        List<Product> products = repository.instancesOf(productCountsMap.keySet());

        int totalPriceFen = 0;
        settlements.clear();
        for (Product product : products) {
            int count = productCountsMap.get(product.getId());
            ProductSettlement settlement = product.settlePrice(count);
            totalPriceFen += settlement.getSettlePriceFen();
            settlements.add(settlement);
        }
        return totalPriceFen;
    }

    public void increaseCurMonthSale(@NotNull Map<LongIdentity, Integer> productCountsMap) {

        List<Product> products = repository.instancesOf(productCountsMap.keySet());

        for (Product product : products) {
            int count = productCountsMap.get(product.getId());
            product.increaseCurMonthSale(count);
            repository.update(product);
        }
    }
}
