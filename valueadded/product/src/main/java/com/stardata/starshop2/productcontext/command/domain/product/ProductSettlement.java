package com.stardata.starshop2.productcontext.command.domain.product;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/4 11:58
 */
@Data
public class ProductSettlement {
    private Product product;
    /** 结算价格 */
    private int settlePriceFen;
    /** 下单数量 */
    private int orderCount;
    /** 结算数量 */
    private Double settleQuantity;
    /** 是否有货 */
    private boolean available;

}
