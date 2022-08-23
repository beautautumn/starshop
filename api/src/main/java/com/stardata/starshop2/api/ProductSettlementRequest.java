package com.stardata.starshop2.api;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ProductSettlementRequest {
    private List<Long> productIds;
    private List<Integer> productCounts;

    public Map<LongIdentity, Integer> composeRequestToMap() {
        List<LongIdentity> ids = this.getProductIds().stream().map(LongIdentity::from).collect(Collectors.toList());

        Map<LongIdentity, Integer> productCountsMap = new HashMap<>();
        for (int i = 0; i < this.getProductIds().size(); i++) {
            productCountsMap.put(ids.get(i), this.getProductCounts().get(i));
        }
        return productCountsMap;
    }}
