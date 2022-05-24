package com.stardata.starshop2.authcontext.pl;

import com.stardata.starshop2.authcontext.domain.user.User;
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
public interface UserResponseMapper {
    UserResponseMapper INSTANCE = Mappers.getMapper(UserResponseMapper.class);

    @Mapping(expression="java(user.currentToken().getToken())", target="token")
    @Mapping(expression="java(user.getId().value())", target="id")
    UserResponse convert(User user);

}
