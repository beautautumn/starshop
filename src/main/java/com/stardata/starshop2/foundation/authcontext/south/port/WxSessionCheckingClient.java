package com.stardata.starshop2.foundation.authcontext.south.port;


import com.stardata.starshop2.foundation.authcontext.domain.WxLoginErrorException;
import com.stardata.starshop2.foundation.authcontext.pl.south.WxSessionCheckingResponse;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/23 23:23
 */
public interface WxSessionCheckingClient {
    WxSessionCheckingResponse code2session(String code) throws WxLoginErrorException;
}
