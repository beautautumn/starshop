package com.stardata.starshop2.ordercontext.command.pl;

import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/29 13:58
 */
@Data
public class OrderSubmitRequest {
    @Data
    public static class Item {
        private long productId;
        private int count;

        Item(long productId, int count) {
            this.productId = productId;
            this.count = count;
        }
    }

    private List<Item> items = new ArrayList<>();

    public Order toOrder(LongIdentity shopId, LongIdentity userId) {
        Order order = Order.createFor(shopId, userId);
        for (Item item : items) {
            order.addItem(LongIdentity.from(item.getProductId()), item.getCount());
        }
        return order;
    }

    public OrderSubmitRequest item(long productId, int count) {
        this.items.add(new Item(productId, count));
        return this;
    }
}
