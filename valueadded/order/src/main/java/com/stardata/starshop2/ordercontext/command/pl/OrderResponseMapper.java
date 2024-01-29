package com.stardata.starshop2.ordercontext.command.pl;

import com.stardata.starshop2.ordercontext.command.domain.order.Order;
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
@Mapper(uses= OrderItemResponseMapper.class)
public interface OrderResponseMapper extends MapStructBaseMapper {
    OrderResponseMapper INSTANCE = Mappers.getMapper(OrderResponseMapper.class);

    @Mapping(expression="java(order.getId().value())", target="id")
    OrderResponse convert(Order order);
}
