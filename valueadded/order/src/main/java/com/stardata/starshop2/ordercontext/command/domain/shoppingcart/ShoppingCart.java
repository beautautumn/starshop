package com.stardata.starshop2.ordercontext.command.domain.shoppingcart;

import com.stardata.starshop2.sharedcontext.domain.AbstractEntity;
import com.stardata.starshop2.sharedcontext.domain.AggregateRoot;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:10
 */
@Getter
@Entity
@Table(name="tb_shopping_cart")
public class ShoppingCart extends AbstractEntity<LongIdentity>  implements AggregateRoot<ShoppingCart> {
    @EmbeddedId
    private LongIdentity id;

    @AttributeOverride(name="id", column = @Column(name="shop_id", nullable = false))
    @Embedded
    private LongIdentity shopId;

    @AttributeOverride(name="id", column = @Column(name="user_id", nullable = false))
    @Embedded
    private LongIdentity userId;

    @Column(updatable = false)
    @CreationTimestamp
    LocalDateTime createTime;

    @UpdateTimestamp
    LocalDateTime updateTime;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    @OrderColumn( name = "displayOrder" )
    private final List<ShoppingCartItem> items = new ArrayList<>();

    @Transient
    private long totalAmountFen;

    @Transient
    private final ShoppingCartCategoryCount categoryCount  = new ShoppingCartCategoryCount();

    protected ShoppingCart(LongIdentity shopId, LongIdentity userId) {
        this.shopId = shopId;
        this.userId = userId;
        this.id = LongIdentity.snowflakeId();
    }

    protected ShoppingCart() {}

    @Override
    public LongIdentity id() {
        return this.id;
    }

    @Override
    public ShoppingCart root() {
        return this;
    }

    public static ShoppingCart of(LongIdentity shopId, LongIdentity userId) {
        return new ShoppingCart(shopId, userId);
    }


    public void addItem(LongIdentity categoryId, LongIdentity productId, int count) {
        ShoppingCartItem item = new ShoppingCartItem(categoryId, productId, count);
        int maxDisplayOrder = this.items.stream().mapToInt(ShoppingCartItem::getDisplayOrder).max().orElse(0) + 1;
        item.setDisplayOrder(maxDisplayOrder);
        this.items.add(item);
    }

    public void removeItem(LongIdentity productId) {
        this.items.removeAll(
                this.items.stream().filter(item -> item.getProductId().equals(productId))
                .collect(Collectors.toList())
        );
    }


    public void refreshItemSubTotal(@NotNull ShoppingCartItem item, @NotNull ShoppingCartItemSubtotal subtotal) {
        if (!this.items.contains(item)) return;

        item.getSubtotal().copyFrom(subtotal);
        this.totalAmountFen = this.items.stream().mapToLong(cartItem->cartItem.getSubtotal().getAmountFen()).sum();

        this.categoryCount.clearAll();
        for (ShoppingCartItem cartItem : this.items) {
            this.categoryCount.addCategoryCount(cartItem.getCategoryId(), cartItem.getCount());
        }

    }
}
