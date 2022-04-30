package com.stardata.starshop2.foundation.authcontext.pl;

import lombok.Data;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/1 00:33
 */
@Data
public class WxEncryptedUserInfo {
    private String encryptedData;
    private String iv;
}
