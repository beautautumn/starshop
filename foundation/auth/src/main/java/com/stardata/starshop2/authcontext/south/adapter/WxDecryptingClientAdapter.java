package com.stardata.starshop2.authcontext.south.adapter;

import com.stardata.starshop2.authcontext.domain.user.UserToken;
import com.stardata.starshop2.authcontext.south.port.WxDecryptingClient;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import com.stardata.starshop2.sharedcontext.domain.MobileNumber;
import org.springframework.stereotype.Component;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/11 11:19
 */
@Adapter( PortType.Client)
@Component
public class WxDecryptingClientAdapter implements WxDecryptingClient {
    @Override
    public MobileNumber decryptMobileNumber(UserToken UserToken, String encryptedData, String iv) {
        return null;
    }
}
