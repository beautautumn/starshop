package com.stardata.starshop2.foundation.authcontext.pl.south;

import lombok.Data;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/27 16:27
 */
@Data
public class WxSessionCheckingResponse {
    private String sessionKey;
    private String openId;
    private String unionId;
    private String errCode;
    private String errMsg;
}
