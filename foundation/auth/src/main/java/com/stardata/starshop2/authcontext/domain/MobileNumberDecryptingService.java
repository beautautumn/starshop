package com.stardata.starshop2.authcontext.domain;

import com.stardata.starshop2.authcontext.domain.user.User;
import com.stardata.starshop2.authcontext.south.port.UserRepository;
import com.stardata.starshop2.authcontext.south.port.WxDecryptingClient;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.domain.MobileNumber;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/1 00:31
 */
@AllArgsConstructor
@Service
public class MobileNumberDecryptingService {
    private UserRepository repository;
    private WxDecryptingClient decryptingClient;

    public MobileNumber decryptWxMobileNumber(LongIdentity userId, String encryptedData, String iv) {
        User user = repository.instanceOf(userId);
        MobileNumber mobileNumber = decryptingClient.decryptMobileNumber(user.currentToken(), encryptedData, iv);
        user.updateMobileNumber(mobileNumber);
        repository.update(user);
        return mobileNumber;
    }
}
