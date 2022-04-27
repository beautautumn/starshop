package com.stardata.starshop2.foundation.authcontext.north.local;

import com.stardata.starshop2.foundation.authcontext.domain.LoginLogService;
import com.stardata.starshop2.foundation.authcontext.domain.WxLoginWithTokenService;
import com.stardata.starshop2.foundation.authcontext.domain.user.User;
import com.stardata.starshop2.foundation.authcontext.domain.user.WxAuthInfo;
import com.stardata.starshop2.foundation.authcontext.pl.north.UserResponse;
import com.stardata.starshop2.foundation.authcontext.pl.north.WxLoginRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/2/24 21:07
 */
@Service
@AllArgsConstructor
public class AuthAppService {
    private WxLoginWithTokenService loginWithTokenService;
    private LoginLogService logService;

    public UserResponse getUserById(String userId) {
        return null;
    }

    public UserResponse loginByWx(WxLoginRequest request) {
        String code = request.getCode();
        WxAuthInfo wxAuthInfo = request.getWxAuthInfo();
        User loginUser = request.toUser();
        User user = loginWithTokenService.loginWithToken(code, wxAuthInfo, loginUser);
        logService.recordLogin(user);
        return UserResponse.from(user);
    }
}
