package com.stardata.starshop2.productcontext.command.domain.productcategory;

import com.stardata.starshop2.productcontext.command.domain.product.Product;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.helper.JSONUtil;

import java.math.BigDecimal;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/4 11:58
 */
public class ProductSettlement {

    private final LongIdentity productId;
    private final String productName;
    private final long settlePriceFen;
    private final int orderCount;
    private final BigDecimal settleQuantity;
    private final boolean available;
    private final String productSnapshot;

    public ProductSettlement(Product product, long settlePriceFen, int orderCount, BigDecimal settleQuantity, boolean available) {
        this.productId = product.getId();
        this.productName = product.getName().toString();
        this.settlePriceFen = settlePriceFen;
        this.orderCount = orderCount;
        this.settleQuantity = settleQuantity;
        this.available = available;
        this.productSnapshot = JSONUtil.toJSONString(product);
    }

    public LongIdentity productId() {
        return productId;
    }

    public String productName() {
        return productName;
    }

    public long settlePriceFen() {
        return settlePriceFen;
    }

    public int orderCount() {
        return orderCount;
    }

    public BigDecimal settleQuantity() {
        return settleQuantity;
    }

    public boolean available() {
        return available;
    }

    public String productSnapshot() {
        return productSnapshot;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductSettlement other)) return false;
        if (o == this) return true;

        // 结算商品购买数量，只支持3位小数
        BigDecimal thousand = new BigDecimal(1000);

        return (other.productId.equals(this.productId)) &&
                (other.productName.equals(this.productName)) &&
                (other.available == this.available) &&
                (other.settlePriceFen == this.settlePriceFen) &&
                (other.orderCount == this.orderCount) &&
                (other.settleQuantity.multiply(thousand).longValue() ==
                        this.settleQuantity.multiply(thousand).longValue());
    }
}
