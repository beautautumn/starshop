package com.stardata.starshop2.productcontext.command.domain.product;

import com.stardata.starshop2.sharedcontext.domain.NonNegativeDecimal;
import com.stardata.starshop2.sharedcontext.domain.PriceFen;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/18 20:19
 */

@Getter
@Embeddable
public class ProductDiscount{
    public enum DISCOUNT_TYPE  {
        NONE('0'), BY_RATE('1'), FIXED_PRICE('2');

        private final Character value;

        DISCOUNT_TYPE(Character value) {
            this.value = value;
        }

        private static final Map<Character, DISCOUNT_TYPE> internalMap = new HashMap<>();

        static {
            for (DISCOUNT_TYPE c : DISCOUNT_TYPE.values()) {
                internalMap.put(c.value, c);
            }
        }

        public Character toCharacter() {
            return this.value;
        }

        public DISCOUNT_TYPE fromCharacter(Character c) {
            return internalMap.get(c);
        }
    }

    public static class DiscountTypeCharConverter implements AttributeConverter<DISCOUNT_TYPE, Character> {
        @Override
        public Character convertToDatabaseColumn(ProductDiscount.DISCOUNT_TYPE attribute) {
            return attribute.toCharacter();
        }

        @Override
        public ProductDiscount.DISCOUNT_TYPE convertToEntityAttribute(Character dbData) {
            return ProductDiscount.DISCOUNT_TYPE.BY_RATE.fromCharacter(dbData);
        }
    }
    @Enumerated
    @Convert(converter = DiscountTypeCharConverter.class)
    private DISCOUNT_TYPE discountType = DISCOUNT_TYPE.NONE;

    @Embedded
    private NonNegativeDecimal discountRate;
    
    @Embedded
    private PriceFen discountPriceFen;

    public ProductDiscount(NonNegativeDecimal discountRate ) {
        this.discountType = DISCOUNT_TYPE.BY_RATE;
        this.discountRate = discountRate;

    }

    public ProductDiscount(PriceFen discountPriceFen ) {
        this.discountType = DISCOUNT_TYPE.FIXED_PRICE;
        this.discountPriceFen = discountPriceFen;

    }

    protected ProductDiscount() {}

    public PriceFen settlePrice(PriceFen originalPriceFen) {
        return switch (discountType) {
            case NONE -> originalPriceFen;
            case BY_RATE -> new PriceFen(discountRate.value().multiply(BigDecimal.valueOf(originalPriceFen.value())).longValue());
            case FIXED_PRICE -> discountPriceFen.value() < originalPriceFen.value() ? discountPriceFen : originalPriceFen;
        };
    }

}
