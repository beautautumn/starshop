package com.stardata.starshop2.authcontext.pl;

import com.stardata.starshop2.sharedcontext.domain.MobileNumber;
import lombok.Data;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/1 00:03
 */
@Data
public class MobileNumberResponse {
    private String mobileNumber;

    MobileNumberResponse(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public static MobileNumberResponse from(MobileNumber mobileNumber) {
        return new MobileNumberResponse(mobileNumber.getValue());
    }
}
