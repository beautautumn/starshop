package com.stardata.starshop2.productcontext.command.domain;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;

import java.math.BigDecimal;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/4 11:58
 */
public record ProductSettlement(LongIdentity productId, long settlePriceFen, int orderCount, BigDecimal settleQuantity,
                                boolean available, String productSnapshot) {

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductSettlement other)) return false;
        if (o == this) return true;

        // 结算商品购买数量，只支持3位小数
        BigDecimal thousand = new BigDecimal(1000);

        return (other.productId.equals(this.productId)) &&
                (other.available == this.available) &&
                (other.settlePriceFen == this.settlePriceFen) &&
                (other.orderCount == this.orderCount) &&
                (other.settleQuantity.multiply(thousand).longValue() ==
                        this.settleQuantity.multiply(thousand).longValue());
    }
}
