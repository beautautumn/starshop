package com.stardata.starshop2.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

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
    
}
