package com.stardata.starshop2.foundation.authcontext.domain.user;

import lombok.Data;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/24 00:09
 */
@Data
public class UserToken {
    private String value;
    UserToken() {}
}
