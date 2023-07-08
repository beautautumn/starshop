package com.stardata.starshop2.api;

import com.stardata.starshop2.pl.ProductSettlementRequest;
import com.stardata.starshop2.pl.ProductSettlementResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Samson Shu
 * @email shush@stardata.top
 * @date  2022/8/15 22:46
 * @version 1.0
 */
@FeignClient(value = "product-biz-services")
public interface ProductBizService {
     @PutMapping("/v2/products/settlements")
     ProductSettlementResponse calcSettlement(@RequestBody ProductSettlementRequest request);
}
