package com.stardata.starshop2.authcontext.north.local;

import com.stardata.starshop2.authcontext.domain.LoginLogService;
import com.stardata.starshop2.authcontext.domain.MobileNumberDecryptingService;
import com.stardata.starshop2.authcontext.domain.WxLoginWithTokenService;
import com.stardata.starshop2.authcontext.domain.user.User;
import com.stardata.starshop2.authcontext.domain.user.WxAuthInfo;
import com.stardata.starshop2.authcontext.pl.UserResponse;
import com.stardata.starshop2.authcontext.pl.WxEncryptedUserInfo;
import com.stardata.starshop2.authcontext.pl.WxLoginRequest;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.domain.MobileNumber;
import com.stardata.starshop2.authcontext.pl.MobileNumberResponse;
import com.stardata.starshop2.sharedcontext.pl.SessionUser;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
    private MobileNumberDecryptingService decryptService;

    public UserResponse loginByWx(@NotNull WxLoginRequest request) {
        String code = request.getCode();
        WxAuthInfo wxAuthInfo = request.getWxAuthInfo();
        User loginUser = request.getRequestUser();
        User user = loginWithTokenService.loginWithToken(code, wxAuthInfo, loginUser);
        logService.recordLogin(user, request.getRequestIp());
        return UserResponse.from(user);
    }

    public MobileNumberResponse decryptWxMobileNumber(SessionUser loginUser, WxEncryptedUserInfo encryptedUserInfo) {
        LongIdentity userId = LongIdentity.from(loginUser.getId());
        String encryptedData = encryptedUserInfo.getEncryptedData();
        String iv = encryptedUserInfo.getIv();

        MobileNumber mobileNumber = decryptService.decryptWxMobileNumber(userId, encryptedData, iv);
        return MobileNumberResponse.from(mobileNumber);
    }
}
