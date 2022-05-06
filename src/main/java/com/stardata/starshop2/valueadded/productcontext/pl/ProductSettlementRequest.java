package com.stardata.starshop2.valueadded.productcontext.pl;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/4 11:46
 */
@Data
public class ProductSettlementRequest {
    private List<Long> productIds;
    private List<Integer> productCounts;

    public Map<LongIdentity, Integer> composeToMap() {
        List<LongIdentity> ids = this.productIds.stream().map(LongIdentity::from).collect(Collectors.toList());

        Map<LongIdentity, Integer> productCountsMap = new HashMap<>();
        for (int i = 0; i < productIds.size(); i++) {
            productCountsMap.put(ids.get(i), productCounts.get(i));
        }
        return productCountsMap;
    }
}
