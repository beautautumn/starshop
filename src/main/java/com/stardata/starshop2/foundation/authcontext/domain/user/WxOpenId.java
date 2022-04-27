package com.stardata.starshop2.foundation.authcontext.domain.user;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 14:54
 */
public class WxOpenId {
    private final String value;

    WxOpenId(String openId){ this.value = openId;}

    public String value() {return this.value;}

    public static WxOpenId of(String openId) {
        return new WxOpenId(openId);
    }
}
