package com.stardata.starshop2.authcontext.domain;

import com.stardata.starshop2.sharedcontext.exception.ApplicationInfrastructureException;
import lombok.Getter;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/27 16:39
 */
@Getter
public class WxLoginErrorException extends ApplicationInfrastructureException {
    private static final int DEFAULT_ERROR_CODE = -99;

    public WxLoginErrorException(String errMsg) {
        super(DEFAULT_ERROR_CODE, errMsg);
    }

    public WxLoginErrorException(int errCode, String errMsg) {
        super(errCode, "Wechat login failed: " + errMsg);
    }

}
