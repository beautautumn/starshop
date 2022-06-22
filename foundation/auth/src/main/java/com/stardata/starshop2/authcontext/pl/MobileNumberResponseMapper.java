package com.stardata.starshop2.authcontext.pl;

import com.stardata.starshop2.sharedcontext.domain.MobileNumber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/24 22:41
 */
@Mapper
public interface MobileNumberResponseMapper {
    MobileNumberResponseMapper INSTANCE = Mappers.getMapper(MobileNumberResponseMapper.class);

    @Mapping(expression="java(mobileNumber.value())", target="mobileNumber")
    MobileNumberResponse convert(MobileNumber mobileNumber);

}
