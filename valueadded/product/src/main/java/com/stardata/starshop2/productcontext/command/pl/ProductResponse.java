package com.stardata.starshop2.productcontext.command.pl;

import com.stardata.starshop2.productcontext.command.domain.product.Product;
import lombok.Data;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/2/28 14:26
 */
@Data
public class ProductResponse {
    public static ProductResponse from(Product product) {
        //todo 完成商品对象转DTO
        return null;
    }
}
