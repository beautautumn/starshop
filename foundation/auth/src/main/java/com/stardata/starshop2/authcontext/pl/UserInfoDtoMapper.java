package com.stardata.starshop2.authcontext.pl;

import com.stardata.starshop2.authcontext.domain.user.User;
import com.stardata.starshop2.pl.UserInfoDto;
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
public interface UserInfoDtoMapper {
    UserInfoDtoMapper INSTANCE = Mappers.getMapper(UserInfoDtoMapper.class);

    @Mapping(expression="java(user.getId().value())", target="id")
    @Mapping(expression="java(user.getGender().value())", target="gender")
    @Mapping(expression="java(user.getMobileNumber().value())", target="mobileNumber")
    @Mapping(expression="java(user.getOpenid().value())", target="openid")
    UserInfoDto convert(User user);

}
