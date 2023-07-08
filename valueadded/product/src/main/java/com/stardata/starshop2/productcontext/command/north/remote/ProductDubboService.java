package com.stardata.starshop2.productcontext.command.north.remote;

import com.stardata.starshop2.api.ProductBizService;
import com.stardata.starshop2.pl.ProductSettlementRequest;
import com.stardata.starshop2.pl.ProductSettlementResponse;
import com.stardata.starshop2.productcontext.command.north.local.ProductAppService;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/8/16 08:47
 */
@DubboService
@AllArgsConstructor
public class ProductDubboService implements ProductBizService {
    private final ProductAppService appService;

    public ProductSettlementResponse calcSettlement(ProductSettlementRequest request)
    {
        return appService.calcSettlement(request);
    }

}
