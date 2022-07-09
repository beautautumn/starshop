package com.stardata.starshop2.authcontext.domain.user;

import javax.persistence.Embeddable;

import java.io.Serializable;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 14:54
 */
@Embeddable
public class WxOpenId implements Serializable {
    private String value;

    WxOpenId(String openId){ this.value = openId;}

    protected WxOpenId() {}

    public String value() {return this.value;}

    public static WxOpenId of(String openId) {
        return new WxOpenId(openId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof WxOpenId that) {
            return this.value.equals(that.value);
        }

        if (o instanceof String str) {
            return this.value.equals(str);
        }
        return false;
    }

    @Override
    public String toString() {
        return value;
    }
}
