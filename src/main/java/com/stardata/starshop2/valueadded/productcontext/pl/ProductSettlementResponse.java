package com.stardata.starshop2.valueadded.productcontext.pl;

import com.stardata.starshop2.valueadded.productcontext.command.domain.product.Product;
import com.stardata.starshop2.valueadded.productcontext.command.domain.product.ProductSettlement;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/4 11:46
 */
@Data
public class ProductSettlementResponse {
    public static ProductSettlementResponse from(List<ProductSettlement> settlements, int totalPriceFen) {
        //todo 商品价格结算map转DTO
        return null;
    }
}
