package com.stardata.starshop2.authcontext.pl;

import com.stardata.starshop2.authcontext.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.net.MalformedURLException;
import java.net.URL;

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
    @Mapping(expression="java(convertStrToUrl(request.getAvatarUrl()))", target="avatarUrl")
    User convert(WxLoginRequest request);

    default URL convertStrToUrl(String urlStr) {
        try {
            return new URL(urlStr);
        } catch (MalformedURLException e) {
            return null;
        }
    }

}
