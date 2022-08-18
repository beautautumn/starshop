package com.stardata.starshop2.productcontext.command.domain.product;

import com.stardata.starshop2.sharedcontext.domain.NonNegativeDecimal;
import com.stardata.starshop2.sharedcontext.domain.PriceFen;
import lombok.Getter;
import org.hibernate.annotations.Type;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.math.BigDecimal;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/18 20:19
 */

@Getter
@Embeddable
public class ProductDiscount{
    @Type(type = "com.stardata.starshop2.productcontext.command.usertype.ProductDiscountTypeUserType")
    private ProductDiscountType discountType = ProductDiscountType.NONE;

    @Embedded
    private NonNegativeDecimal discountRate;
    
    @Embedded
    private PriceFen discountPriceFen;

    public ProductDiscount(NonNegativeDecimal discountRate ) {
        this.discountType = ProductDiscountType.BY_RATE;
        this.discountRate = discountRate;

    }

    public ProductDiscount(PriceFen discountPriceFen ) {
        this.discountType = ProductDiscountType.FIXED_PRICE;
        this.discountPriceFen = discountPriceFen;

    }

    protected ProductDiscount() {}

    public PriceFen settlePrice(PriceFen originalPriceFen) {
        if (this.discountType == null) return originalPriceFen;

        if (discountType == ProductDiscountType.BY_RATE) {
            return new PriceFen(discountRate.value().multiply(BigDecimal.valueOf(originalPriceFen.value())).longValue());
        }
        else if (discountType == ProductDiscountType.FIXED_PRICE) {
            return discountPriceFen.value() < originalPriceFen.value() ? discountPriceFen : originalPriceFen;
        }
        return originalPriceFen;
    }

}
