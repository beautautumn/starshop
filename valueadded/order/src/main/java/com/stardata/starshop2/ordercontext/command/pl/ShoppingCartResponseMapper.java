package com.stardata.starshop2.ordercontext.command.pl;

import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCart;
import com.stardata.starshop2.sharedcontext.pl.MapStructBaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/28 09:19
 */
@Mapper(uses=ShoppingCartResponseItemMapper.class)
public interface ShoppingCartResponseMapper extends MapStructBaseMapper {
    ShoppingCartResponseMapper INSTANCE = Mappers.getMapper(ShoppingCartResponseMapper.class);

    ShoppingCartResponse convert(ShoppingCart shoppingCart);
}
