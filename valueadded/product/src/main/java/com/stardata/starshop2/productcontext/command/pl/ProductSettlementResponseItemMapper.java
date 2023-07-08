package com.stardata.starshop2.productcontext.command.pl;

import com.stardata.starshop2.pl.ProductSettlementResponse;
import com.stardata.starshop2.productcontext.command.domain.product.ProductSettlement;
import com.stardata.starshop2.sharedcontext.pl.MapStructBaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/8/14 13:35
 */
@Mapper
public interface ProductSettlementResponseItemMapper extends MapStructBaseMapper {
    ProductSettlementResponseItemMapper INSTANCE = Mappers.getMapper(ProductSettlementResponseItemMapper.class);

    @Mapping(expression="java(settlement.productId().value())", target="id")
    @Mapping(expression="java(settlement.productName())", target="name")
    @Mapping(expression="java(settlement.settlePriceFen())", target="priceFen")
    @Mapping(expression="java(settlement.orderCount())", target="count")
    @Mapping(expression="java(settlement.settleQuantity())", target="quantity")
    @Mapping(expression="java(settlement.available())", target="available")
    @Mapping(expression="java(settlement.productSnapshot())", target="productSnapshot")
    ProductSettlementResponse.Item convert(ProductSettlement settlement);
}
