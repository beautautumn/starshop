package com.stardata.starshop2.ordercontext.command.pl;

import com.stardata.starshop2.ordercontext.command.domain.order.PrepayOrder;
import com.stardata.starshop2.sharedcontext.pl.MapStructBaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/7/2 11:36
 */
@Mapper
public interface PrepayOrderResponseMapper extends MapStructBaseMapper {
    PrepayOrderResponseMapper INSTANCE = Mappers.getMapper(PrepayOrderResponseMapper.class);

    PrepayOrderResponse convert(PrepayOrder order);
}
