package com.stardata.starshop2.authcontext.domain.user;

import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/27 12:16
 */
@Data
public class WxAuthInfo {
    //进行微信登录认证时的请求信息
    private String rawData;
    private String signature;

    //完成微信登录认证后返回的信息
    private String openid;
    private String unionid;
    private String sessionKey;

    public WxAuthInfo( String rawData, String signature){
        this.rawData = rawData;
        this.signature = signature;
    }

    public boolean checkIntegrity(String sessionKey) {
        final String generatedSignature = DigestUtils.sha1Hex(this.rawData + sessionKey);
        return generatedSignature.equals(this.signature);
    }
}
