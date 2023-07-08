package com.stardata.starshop2.sharedcontext.domain;

import com.stardata.starshop2.sharedcontext.exception.ApplicationValidationException;
import com.stardata.starshop2.sharedcontext.usertype.PersistentCharEnum;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/8 19:25
 */
public enum Gender implements PersistentCharEnum {
    UNKNOWN('0'), MALE('1'), FEMALE('2');

    private final Character value;

    Gender(Character value) {
        this.value = value;
    }

    public static Gender of(Character value) {
        for (Gender gender : Gender.values()) {
            if (gender.value.equals(value)) {
                return gender;
            }
        }
        throw new ApplicationValidationException("Invalid gender value "+value);
    }

    public static Gender of(Integer value) {
        for (Gender gender : Gender.values()) {
            if (gender.ordinal() == value) {
                return gender;
            }
        }
        throw new ApplicationValidationException("Invalid gender value "+value);
    }

    @Override
    public char value() {
        return this.value;
    }
}
