package com.stardata.starshop2.ordercontext.command.pl;

import com.stardata.starshop2.ordercontext.command.domain.order.PayResult;
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
public interface OrderPayResultRequestMapper extends MapStructBaseMapper {
    OrderPayResultRequestMapper INSTANCE = Mappers.getMapper(OrderPayResultRequestMapper.class);

    PayResult convert(OrderPayResultRequest request);
}
