package com.stardata.starshop2.foundation.authcontext.north.remote;

import com.stardata.starshop2.foundation.authcontext.north.local.AuthAppService;
import com.stardata.starshop2.foundation.authcontext.pl.UserResponse;
import com.stardata.starshop2.foundation.authcontext.pl.WxEncryptedUserInfo;
import com.stardata.starshop2.foundation.authcontext.pl.WxLoginRequest;
import com.stardata.starshop2.sharedcontext.annotation.IgnoreAuth;
import com.stardata.starshop2.sharedcontext.annotation.LoginUser;
import com.stardata.starshop2.sharedcontext.pl.MobileNumberResponse;
import com.stardata.starshop2.sharedcontext.pl.SessionUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/2/24 21:07
 */
@Api(tags = "登录授权资源接口")
@RestController
@RequestMapping("/v2/auth")
@AllArgsConstructor
public class AuthResource {
    private final AuthAppService appService;

    @PostMapping("/wxlogin")
    @IgnoreAuth
    public ResponseEntity<UserResponse> loginByWx(WxLoginRequest request) {
        UserResponse response = appService.loginByWx(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/wxphone")
    @ApiOperation(value = "获取用户微信绑定的手机号")
    public  ResponseEntity<MobileNumberResponse> decryptWxMobileNumber(@LoginUser SessionUser loginUser,
                                                                       WxEncryptedUserInfo encryptedUserInfo) {
        MobileNumberResponse response = appService.decryptWxMobileNumber(loginUser, encryptedUserInfo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
