package com.stardata.starshop2.authcontext.south.adapter;

import com.stardata.starshop2.authcontext.domain.WxLoginErrorException;
import com.stardata.starshop2.authcontext.domain.user.WxAuthInfo;
import com.stardata.starshop2.authcontext.south.port.WxSessionCheckingClient;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/11 11:19
 */
@Adapter(PortType.Client)
public class WxSessionCheckingClientAdapter implements WxSessionCheckingClient {
    @Override
    public void code2session(String code, WxAuthInfo wxAuthInfo) throws WxLoginErrorException {

    }
}
