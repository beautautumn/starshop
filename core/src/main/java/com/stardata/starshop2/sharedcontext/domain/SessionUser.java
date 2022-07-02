package com.stardata.starshop2.sharedcontext.domain;

import lombok.Data;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/26 18:00
 */
@Data
public class SessionUser {
    private LongIdentity id;
    private String openid;


    protected SessionUser(LongIdentity id, String openid) {
        this.id = id;
        this.openid = openid;
    }

    public static SessionUser from(Long id) {
        return new SessionUser(LongIdentity.from(id), "");
    }

    public static SessionUser from(Long id, String openid) {
        return new SessionUser(LongIdentity.from(id), openid);
    }
}
