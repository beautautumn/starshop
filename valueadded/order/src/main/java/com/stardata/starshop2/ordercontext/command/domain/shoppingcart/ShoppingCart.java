package com.stardata.starshop2.ordercontext.command.domain.shoppingcart;

import com.stardata.starshop2.sharedcontext.domain.AbstractEntity;
import com.stardata.starshop2.sharedcontext.domain.AggregateRoot;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
@AttributeOverrides({
        @AttributeOverride(name = "shopId.id", column = @Column(name = "shop_id", nullable = false)),
        @AttributeOverride(name = "userId.id", column = @Column(name = "user_id", nullable = false)),
})

public class ShoppingCart extends AbstractEntity<LongIdentity>  implements AggregateRoot<ShoppingCart> {
    @EmbeddedId
    private LongIdentity id;

    @Embedded
    @Setter
    private LongIdentity shopId;

    @Embedded
    @Setter
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

    public ShoppingCart() {}

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
        ShoppingCartItem item = new ShoppingCartItem(this, categoryId, productId, count);
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

    public void refreshItemsSubTotal(Map<LongIdentity, ShoppingCartItemSubtotal> itemsSubtotal) {
        List<LongIdentity> itemIds = this.items.stream().map(ShoppingCartItem::getProductId).collect(Collectors.toList());
        itemsSubtotal.forEach(
                (productId, subtotal) -> {
                    int idx = itemIds.indexOf(productId);
                    if (idx > -1) {
                        ShoppingCartItem cartItem = this.items.get(idx);
                        cartItem.getSubtotal().copyFrom(subtotal);
                    }
                }
        );

        this.totalAmountFen = this.items.stream().mapToLong(cartItem->cartItem.getSubtotal().getAmountFen()).sum();

        this.categoryCount.clearAll();
        for (ShoppingCartItem cartItem : this.items) {
            this.categoryCount.addCategoryCount(cartItem.getCategoryId(), cartItem.getCount());
        }
    }
}
