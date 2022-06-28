package com.stardata.starshop2.ordercontext.command.domain.shoppingcart;

import com.stardata.starshop2.sharedcontext.domain.NonNegativeDecimal;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:11
 */
@Data
public class ShoppingCartItemSubtotal {
    private boolean available;
    private long amountFen;
    private int orderCount;
    private NonNegativeDecimal totalQuantity;

    public void copyFrom(@NotNull ShoppingCartItemSubtotal subtotal) {
        this.available = subtotal.available;
        this.amountFen = subtotal.amountFen;
        this.orderCount = subtotal.orderCount;
        this.totalQuantity = subtotal.totalQuantity;
    }

    public ShoppingCartItemSubtotal(int orderCount, long amountFen, NonNegativeDecimal totalQuantity, boolean available) {
        this.orderCount = orderCount;
        this.available = available;
        this.amountFen = amountFen;
        this.totalQuantity = totalQuantity;
    }

    public ShoppingCartItemSubtotal() {
        this.orderCount = 0;
        this.available = false;
        this.amountFen = 0;
        this.totalQuantity = new NonNegativeDecimal("0");
    }

}
