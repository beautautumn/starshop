package com.stardata.starshop2.ordercontext.command.pl;

import com.stardata.starshop2.ordercontext.command.domain.order.OrderItem;
import com.stardata.starshop2.sharedcontext.pl.MapStructBaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/7/2 11:36
 */
@Mapper
public interface OrderItemResponseMapper extends MapStructBaseMapper {
    OrderItemResponseMapper INSTANCE = Mappers.getMapper(OrderItemResponseMapper.class);

    @Mapping(source="purchaseCount", target="count")
    @Mapping(source="subtotalFen", target="amountFen")
    OrderResponse.Item convert(OrderItem item);
}
