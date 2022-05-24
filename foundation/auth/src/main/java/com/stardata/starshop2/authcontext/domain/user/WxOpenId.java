package com.stardata.starshop2.authcontext.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 14:54
 */
@Embeddable
public class WxOpenId {
    @Column(name = "openid", nullable = false)
    private String value;

    WxOpenId(String openId){ this.value = openId;}

    protected WxOpenId() {}

    public String value() {return this.value;}

    public static WxOpenId of(String openId) {
        return new WxOpenId(openId);
    }

    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof WxOpenId) {
            return this.value.equals(o.toString());
        }
        return false;
    }

    public String toString() {
        return value;
    }
}
