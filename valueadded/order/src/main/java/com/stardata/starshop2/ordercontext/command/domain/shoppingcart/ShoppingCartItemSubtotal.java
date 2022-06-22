package com.stardata.starshop2.ordercontext.command.domain.shoppingcart;

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
    private long totalQuantity;

    public void copyFrom(@NotNull ShoppingCartItemSubtotal subtotal) {
        this.available = subtotal.available;
        this.amountFen = subtotal.amountFen;
        this.totalQuantity = subtotal.totalQuantity;
    }

    ShoppingCartItemSubtotal(long amountFen, long totalQuantity, boolean available) {
        this.available = available;
        this.amountFen = amountFen;
        this.totalQuantity = totalQuantity;
    }

    public static ShoppingCartItemSubtotal from(long amountFen, long subtotal, boolean available){
        return new ShoppingCartItemSubtotal(amountFen, subtotal, available);

    }
}
