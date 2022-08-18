package com.stardata.starshop2.ordercontext.command.pl;

import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCart;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/26 18:04
 */
@Data
public class ShoppingCartResponse {

    @Data
    public static class Item{
        private Long categoryId;
        private Long productId;
        private Integer count;
        private boolean available;
        private Integer orderCount;
        private Long amountFen;
        private BigDecimal totalQuantity;

        public Long categoryId() {return categoryId;}
        public Long productId() {return productId;}
        public Integer count() {return count;}
        public boolean available() {return available;}
        public Integer orderCount() {return orderCount;}
        public Long amountFen() {return amountFen;}
        public BigDecimal totalQuantity() {return totalQuantity;}
    }

    private long totalAmountFen;
    private List<Item> items = new ArrayList<>();

    public static ShoppingCartResponse from(ShoppingCart shoppingCart) {
        return ShoppingCartResponseMapper.INSTANCE.convert(shoppingCart);
    }
}
