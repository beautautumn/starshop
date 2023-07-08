package com.stardata.starshop2.customercontext.pl;

import com.stardata.starshop2.customercontext.domain.customer.Customer;
import com.stardata.starshop2.pl.CustomerInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/8 20:24
 */
@Mapper
public interface CustomerToDtoMapper {
    CustomerToDtoMapper INSTANCE = Mappers.getMapper(CustomerToDtoMapper.class);

    @Mapping(expression="java(customer.getId().value())", target="id")
    @Mapping(expression="java(customer.getShopId().value())", target="shopId")
    @Mapping(expression="java(customer.getUserId().value())", target="userId")
    @Mapping(expression="java(customer.avatarUrl().toString())", target="avatarUrl")
    @Mapping(expression="java(customer.getGender().value())", target="gender")
    @Mapping(expression="java(customer.getMobilePhone().value())", target="mobilePhone")
    @Mapping(expression="java(customer.labels())", target="labels")
    CustomerInfoDto convert(Customer customer);
}
