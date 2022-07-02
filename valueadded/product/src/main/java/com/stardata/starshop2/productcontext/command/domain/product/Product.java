package com.stardata.starshop2.productcontext.command.domain.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stardata.starshop2.productcontext.command.domain.productcategory.ProductCategory;
import com.stardata.starshop2.productcontext.command.domain.productcategory.ProductSettlement;
import com.stardata.starshop2.sharedcontext.domain.*;
import com.stardata.starshop2.sharedcontext.exception.DomainException;
import com.stardata.starshop2.sharedcontext.helper.BooleanCharConverter;
import com.stardata.starshop2.sharedcontext.helper.Constants;
import com.stardata.starshop2.sharedcontext.helper.JSONUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:31
 */
@Getter
@Entity
@Table(name="tb_product2")
@SQLDelete(sql = "update tb_product2 set is_valid = '"+ Constants.DELETE_FLAG.DELETED+"' where id = ?")
@Where(clause = "is_valid = '"+ Constants.DELETE_FLAG.NORMAL+"'")
@AttributeOverrides({
        @AttributeOverride(name = "shopId.id", column = @Column(name = "shop_id", nullable = false)),
        @AttributeOverride(name = "introduction.value", column = @Column(name = "introduction", length = 500)),
        @AttributeOverride(name = "minPurchase.value", column = @Column(name = "min_purchase")),
        @AttributeOverride(name = "purchaseLimit.value", column = @Column(name = "purchase_limit")),
        @AttributeOverride(name = "originalPriceFen.value", column = @Column(name = "original_price_fen")),
        @AttributeOverride(name = "discount.discountType", column = @Column(name = "discount_type", length = 1)),
        @AttributeOverride(name = "discount.discountRate.value", column = @Column(name = "discount_rate")),
        @AttributeOverride(name = "discount.discountPriceFen.value", column = @Column(name = "discount_price_fen")),
})
public class Product  extends AbstractEntity<LongIdentity> implements AggregateRoot<Product> {
    @EmbeddedId
    private LongIdentity id;

    @Embedded
    private LongIdentity shopId;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private ProductCategory category;

    @Embedded
    @Setter
    private ProductName name;

    @Embedded
    private ProductDescription introduction;

    @Getter(AccessLevel.NONE)
    private String unit;

    @Embedded
    private NonNegativeDecimal minPurchase;

    @Embedded
    private NonNegativeInteger purchaseLimit;

    @Embedded
    private PriceFen originalPriceFen;

    private Long displayOrder;

    @Getter(AccessLevel.NONE)
    private String labels;

    @Convert(converter = BooleanCharConverter.class)
    private boolean isAvailable;

    @Convert(converter = BooleanCharConverter.class)
    private boolean onShelves;

    @Embedded
    private ProductDiscount discount;

    @Column(updatable = false)
    @CreationTimestamp
    LocalDateTime createTime;

    @UpdateTimestamp
    LocalDateTime updateTime;

    @Override
    public LongIdentity id() {
        return this.id;
    }

    @Override
    public Product root() {
        return this;
    }

    private Product(LongIdentity shopId, ProductCategory category, String name, ProductUoM unit) {
        this.id = LongIdentity.snowflakeId();
        this.shopId = shopId;
        this.category = category;
        this.name = ProductName.of(name);
        this.unit = unit.toString();
        this.minPurchase = new NonNegativeDecimal("1.0");
    }

    protected Product() {}

    public static Product from(LongIdentity shopId, ProductCategory category, String name, ProductUoM unit){
        return new Product(shopId, category, name, unit);
    }

    public ProductUoM getUnit() {
        return ProductUoM.from(this.unit);
    }

    public Product minPurchase(NonNegativeDecimal minPurchase) {
        this.minPurchase = minPurchase;
        return this;
    }

    public Product purchaseLimit(NonNegativeInteger purchaseLimit) {
        this.purchaseLimit = purchaseLimit;
        return this;
    }

    public Product originalPriceFen(PriceFen originalPriceFen) {
        this.originalPriceFen = originalPriceFen;
        return this;
    }

    public Product displayOrder(Long displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    public Product introduction(String introduction) {
        this.introduction = new ProductDescription(introduction);
        return this;
    }

    public Product labels(List<String> labels) {
        this.labels = JSONUtil.toJSONString(labels);
        return this;
    }

    public List<String> labels() {
        List<String> result = null;
        if (this.labels != null) {
            try {
                result = JSONUtil.parseList(this.labels, String.class);
            } catch (JsonProcessingException ignore) {}
        }
        return result;
    }

    public Product isAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
        return this;
    }

    public Product onShelves(boolean onShelves) {
        this.onShelves = onShelves;
        return this;
    }

    public Product discount(ProductDiscount discount) {
        switch(discount.getDiscountType()) {
            case BY_RATE:
                BigDecimal multiple = new BigDecimal(100);
                if (discount.getDiscountRate().value().multiply(multiple).longValue() > BigDecimal.ONE.longValue()*100) {
                    throw new DomainException("Discount rate must less then 1.0.");
                }
                break;
            case FIXED_PRICE:
                if (discount.getDiscountPriceFen().value() > this.originalPriceFen.value()) {
                    throw new DomainException(
                            String.format("Discount fixed price [%d] must less then original price [%d].",
                                    discount.getDiscountPriceFen().value(),
                                    this.originalPriceFen.value())
                    );
                }
                break;
        }
        this.discount = discount;
        return this;
    }

    public ProductSettlement settlePrice(int count) {
        boolean available = this.isAvailable && this.onShelves;
        BigDecimal settleQuantity = BigDecimal.ZERO;
        int orderCount = 0;
        PriceFen settlePriceFen = new PriceFen(0);
        if (available) {
            orderCount = (this.purchaseLimit != null) && (count > this.purchaseLimit.value())?
                    this.purchaseLimit.value(): count;

            settleQuantity = this.minPurchase.value().multiply(BigDecimal.valueOf(orderCount));

            if ((this.discount != null)) {
                settlePriceFen = this.discount.settlePrice(this.originalPriceFen).multiply(orderCount);
            } else {
                settlePriceFen = this.originalPriceFen.multiply(orderCount);
            }
        }

        return new ProductSettlement(this, settlePriceFen.value(),orderCount, settleQuantity, available);
    }


    public void increaseCurMonthSale(int count) {
        synchronized(this) {
            //todo 完成商品新增当月销量方法（要求线程安全）

        }
    }
}
