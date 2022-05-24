package com.stardata.starshop2.authcontext.pl;

import com.stardata.starshop2.authcontext.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/24 19:33
 */
@Mapper
public interface WxLoginRequestMapper {
    WxLoginRequestMapper INSTANCE = Mappers.getMapper(WxLoginRequestMapper.class);

    @Mapping(target = "openid", ignore = true)

    User convert(WxLoginRequest request);

}
