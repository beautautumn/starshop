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

    public record Item(Long categoryId, Long productId, Integer count,
                       boolean available, Integer orderCount, Long amountFen, BigDecimal totalQuantity ) { }

    private long totalAmountFen;
    private List<Item> items = new ArrayList<>();

    public static ShoppingCartResponse from(ShoppingCart shoppingCart) {
        return ShoppingCartResponseMapper.INSTANCE.convert(shoppingCart);
    }
}
