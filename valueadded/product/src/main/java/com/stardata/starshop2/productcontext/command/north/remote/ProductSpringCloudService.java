package com.stardata.starshop2.productcontext.command.north.remote;

import com.stardata.starshop2.api.ProductBizService;
import com.stardata.starshop2.pl.ProductSettlementRequest;
import com.stardata.starshop2.pl.ProductSettlementResponse;
import com.stardata.starshop2.productcontext.command.north.local.ProductAppService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/8/16 08:47
 */
@RestController
@AllArgsConstructor
@RequestMapping("/v2/products")
public class ProductSpringCloudService implements ProductBizService {
    private final ProductAppService appService;

    @PutMapping("/settlements")
    @Override
    public ProductSettlementResponse calcSettlement(@RequestBody ProductSettlementRequest request)
    {
        return appService.calcSettlement(request);
    }

}
