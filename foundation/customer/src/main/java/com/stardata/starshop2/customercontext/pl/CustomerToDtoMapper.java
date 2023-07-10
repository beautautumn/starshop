package com.stardata.starshop2.customercontext.pl;

import com.stardata.starshop2.customercontext.domain.customer.Customer;
import com.stardata.starshop2.pl.CustomerInfoDto;
import com.stardata.starshop2.sharedcontext.domain.Gender;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.domain.MobileNumber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/8 20:24
 */
@Mapper(imports = {LongIdentity.class, MobileNumber.class, Gender.class})
public interface CustomerToDtoMapper {
    CustomerToDtoMapper INSTANCE = Mappers.getMapper(CustomerToDtoMapper.class);

    @Mapping(expression="java(request.getId().value())", target="id")
    @Mapping(expression="java(request.getShopId().value())", target="shopId")
    @Mapping(expression="java(request.getUserId().value())", target="userId")
    @Mapping(expression="java(request.getGender().value())", target="gender")
    @Mapping(expression="java(request.getMobilePhone().value())", target="mobilePhone")
    @Mapping(expression="java(request.labels())", target="labels")
    CustomerInfoDto convert(Customer request);

    @Mapping(expression="java(LongIdentity.from(request.getId()))", target="id")
    @Mapping(expression="java(LongIdentity.from(request.getShopId()))", target="shopId")
    @Mapping(expression="java(LongIdentity.from(request.getUserId()))", target="userId")
    @Mapping(expression="java(Gender.of(request.getGender()))", target="gender")
    @Mapping(expression="java(MobileNumber.from(request.getMobilePhone()))", target="mobilePhone")
    @Mapping(target="hasRead", ignore = true)
    @Mapping(target="isValid", ignore = true)
    @Mapping(target="createTime", ignore = true)
    @Mapping(target="updateTime", ignore = true)
    Customer convert(CustomerInfoDto request, @MappingTarget Customer customer);

}
