package com.stardata.starshop2.sharedcontext.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/6 11:27
 */
@Embeddable
public class StringIdentity implements Identity<String>{
    private String code;

    protected StringIdentity(String code) {
        this.code = code;
    }

    protected StringIdentity() {
        this.code = "";
    }

    public static StringIdentity from(String key) {
        return new StringIdentity(key);
    }

    @Override
    public String value() {
        return this.code;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StringIdentity) {
            StringIdentity that = (StringIdentity)o;
            return this.code.equals(that.code);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.code);
    }
}
