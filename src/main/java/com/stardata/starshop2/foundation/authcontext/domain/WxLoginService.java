package com.stardata.starshop2.foundation.authcontext.domain;

import com.stardata.starshop2.foundation.authcontext.domain.user.WxAuthInfo;
import com.stardata.starshop2.foundation.authcontext.domain.user.WxOpenId;
import com.stardata.starshop2.foundation.authcontext.south.port.WxSessionCheckingClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/23 23:21
 */
@AllArgsConstructor
@Service
public class WxLoginService {
    private final WxSessionCheckingClient authClient;

    public WxOpenId wxLogin(String code, WxAuthInfo wxAuthInfo) throws WxLoginErrorException{
        authClient.code2session(code, wxAuthInfo);
        if (!wxAuthInfo.checkIntegrity(wxAuthInfo.getSessionKey())) {
            throw new WxLoginErrorException("Checking userinfo integrity failed.");
        }
        return WxOpenId.of(wxAuthInfo.getOpenId());
    }
}
