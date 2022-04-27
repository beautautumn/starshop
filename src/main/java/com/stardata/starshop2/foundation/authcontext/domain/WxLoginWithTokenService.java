package com.stardata.starshop2.foundation.authcontext.domain;

import com.stardata.starshop2.foundation.authcontext.domain.user.User;
import com.stardata.starshop2.foundation.authcontext.domain.user.WxAuthInfo;
import com.stardata.starshop2.foundation.authcontext.domain.user.WxOpenId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/27 12:14
 */
@AllArgsConstructor
@Service
public class WxLoginWithTokenService {
    private final WxLoginService wxLoginService;
    private final UserTokenService userTokenService;
    private final UserExistenceService userExistenceService;

    public User loginWithToken(String code, WxAuthInfo wxAuthInfo, User loginUser) {
        WxOpenId openid = wxLoginService.wxLogin(code, wxAuthInfo);
        User user = userExistenceService.ensureUser(openid, loginUser);
        userTokenService.generateLoginToken(user);
        return user;
    }
}
