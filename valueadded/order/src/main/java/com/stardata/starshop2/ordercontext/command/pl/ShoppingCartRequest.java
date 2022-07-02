package com.stardata.starshop2.ordercontext.command.pl;

import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCart;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/26 18:04
 */
@Data
public class ShoppingCartRequest {
    @Data
    public static class Item {
        private Long categoryId;
        private Long productId;
        private Integer count;

        public Item(long categoryId, long productId, int count) {
            this.categoryId = categoryId;
            this.productId = productId;
            this.count = count;
        }
    }

    private List<Item> items = new ArrayList<>();

    public ShoppingCart toShoppingCart(LongIdentity shopId, LongIdentity userId) {
        ShoppingCart shoppingCart = ShoppingCart.of(shopId, userId);
        for (ShoppingCartRequest.Item item : items) {
            shoppingCart.addItem(LongIdentity.from(item.getCategoryId()), LongIdentity.from(item.getProductId()), item.getCount());
        }
        return shoppingCart;
    }

    public ShoppingCartRequest item(long categoryId, long productId, int count) {
        Item item = new Item(categoryId, productId, count);
        this.items.add(item);
        return this;
    }
}
