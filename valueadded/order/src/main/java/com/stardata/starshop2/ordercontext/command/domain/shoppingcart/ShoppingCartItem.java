package com.stardata.starshop2.ordercontext.command.domain.shoppingcart;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
public class ShoppingCartItem {
    @EmbeddedId
    private LongIdentity id;

    private long categoryId;
    private long productId;
    private int count;
    @Setter
    private int displayOrder;

    @Transient
    private final ShoppingCartItemSubtotal subtotal = new ShoppingCartItemSubtotal();


    public LongIdentity getProductId() {
        return LongIdentity.from(this.productId);
    }

    public void setProductId(LongIdentity productId) {
        this.productId = productId.value();
    }

    public LongIdentity getCategoryId() {
        return LongIdentity.from(this.categoryId);
    }

    public void setCategoryIdId(LongIdentity categoryId) {
        this.categoryId = categoryId.value();
    }

    ShoppingCartItem(LongIdentity categoryId, LongIdentity productId, int count) {
        this.id = LongIdentity.snowflakeId();
        this.categoryId = categoryId.value();
        this.productId = productId.value();
        this.count = count;
        this.displayOrder = 0;
    }

    protected ShoppingCartItem() {}
}
