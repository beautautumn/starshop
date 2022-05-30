package com.stardata.starshop2.authcontext.domain.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 14:54
 */
@Data
public class UserStatus implements Serializable {
    private boolean hasManageableShop;
    private boolean hasManageableChain;
    private boolean hasJoinedChain;
    UserStatus() {}
}
