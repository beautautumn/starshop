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

    private final int errCode;
    private String errMsg;


    public WxLoginErrorException(String errMsg) {
        super(errMsg);
        this.errCode = DEFAULT_ERROR_CODE;
    }

    public WxLoginErrorException(int errCode, String errMsg) {
        super("Wechat login failed: " + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

}
