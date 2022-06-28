package com.stardata.starshop2.ordercontext.command.pl;

import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCartItem;
import com.stardata.starshop2.sharedcontext.pl.MapStructBaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/28 09:23
 */
@Mapper
public interface ShoppingCartResponseItemMapper extends MapStructBaseMapper {
    @Mapping(source="subtotal.available", target="available")
    @Mapping(source="subtotal.orderCount", target="orderCount")
    @Mapping(source="subtotal.amountFen", target="amountFen")
    @Mapping(expression="java(item.getSubtotal().getTotalQuantity().value())", target="totalQuantity")
    ShoppingCartResponse.Item convert(ShoppingCartItem item);
}
