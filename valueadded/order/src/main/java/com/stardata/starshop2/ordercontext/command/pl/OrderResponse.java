package com.stardata.starshop2.ordercontext.command.pl;

import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/29 13:57
 */
@Data
public class OrderResponse {
    @Data
    public static class Item{
        private Long productId;
        private Integer count;
        private Long amountFen;

        public Long productId(){return productId;}
        public Integer count(){return count;}
        public Long amountFen(){return amountFen;}
    }

    private long totalAmountFen;
    private List<Item> items = new ArrayList<>();


    public static OrderResponse from(Order order) {
        return OrderResponseMapper.INSTANCE.convert(order);
    }
}
