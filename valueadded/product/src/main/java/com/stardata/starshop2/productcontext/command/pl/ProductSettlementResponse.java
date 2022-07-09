package com.stardata.starshop2.productcontext.command.pl;

import com.stardata.starshop2.productcontext.command.domain.productcategory.ProductSettlement;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import lombok.Data;

import java.util.Map;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/4 11:46
 */
@Data
public class ProductSettlementResponse {
    public static ProductSettlementResponse from(Map<LongIdentity, ProductSettlement> settlements) {
        //todo 商品价格结算map转DTO
        return null;
    }
}
