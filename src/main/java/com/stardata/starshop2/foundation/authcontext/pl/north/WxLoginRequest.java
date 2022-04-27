package com.stardata.starshop2.foundation.authcontext.pl.north;

import com.stardata.starshop2.foundation.authcontext.domain.user.User;
import com.stardata.starshop2.foundation.authcontext.domain.user.WxAuthInfo;
import lombok.Data;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/23 23:20
 */
@Data
public class WxLoginRequest {
    /**
     * 微信授权相关字段
     */
    private String code;
    private String rawData;
    private String signature;

    /**
     * 微信用户信息相关字段
     */
    private String avatarUrl;
    private String country;
    private String province;
    private String city;
    private String language;
    private Integer gender;
    private String nickName;

    public WxAuthInfo getWxAuthInfo() {
        return new WxAuthInfo(rawData, signature);
    }


    public User toUser() {
        return User.of(nickName, gender)
                .avatarUrl(avatarUrl)
                .country(country)
                .province(province)
                .city(city)
                .language(language);
    }
}
