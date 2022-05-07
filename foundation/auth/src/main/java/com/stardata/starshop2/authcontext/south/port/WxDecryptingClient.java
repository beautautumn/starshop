package com.stardata.starshop2.authcontext.south.port;

import com.stardata.starshop2.authcontext.domain.user.UserToken;
import com.stardata.starshop2.sharedcontext.domain.MobileNumber;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/1 00:38
 */
public interface WxDecryptingClient {
    MobileNumber decryptMobileNumber(UserToken UserToken, String encryptedData, String iv);
}
