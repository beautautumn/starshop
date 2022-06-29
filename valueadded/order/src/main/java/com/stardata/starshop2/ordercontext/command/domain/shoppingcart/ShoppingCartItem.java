package com.stardata.starshop2.ordercontext.command.domain.shoppingcart;

import com.stardata.starshop2.sharedcontext.domain.AbstractEntity;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:10
 */
@Entity
@Table(name="tb_shopping_cart_item")
@Getter
public class ShoppingCartItem  extends AbstractEntity<LongIdentity> {
    @EmbeddedId
    private LongIdentity id;

    @AttributeOverride(name="id", column = @Column(name="category_id", nullable = false))
    @Embedded
    private LongIdentity categoryId;

    @AttributeOverride(name="id", column = @Column(name="product_id", nullable = false))
    @Embedded
    private LongIdentity productId;

    private int count;
    @Setter
    private int displayOrder;

    @Transient
    private final ShoppingCartItemSubtotal subtotal = new ShoppingCartItemSubtotal();


    public LongIdentity getProductId() {
        return this.productId;
    }

    public void setProductId(LongIdentity productId) {
        this.productId = productId;
    }

    public LongIdentity getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryIdId(LongIdentity categoryId) {
        this.categoryId = categoryId;
    }

    ShoppingCartItem(LongIdentity categoryId, LongIdentity productId, int count) {
        this.id = LongIdentity.snowflakeId();
        this.categoryId = categoryId;
        this.productId = productId;
        this.count = count;
        this.displayOrder = 0;
    }

    protected ShoppingCartItem() {}

    @Override
    public LongIdentity id() {
        return this.id;
    }
}
