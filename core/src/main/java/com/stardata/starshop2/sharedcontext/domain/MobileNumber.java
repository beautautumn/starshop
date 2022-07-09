package com.stardata.starshop2.sharedcontext.domain;

import com.stardata.starshop2.sharedcontext.exception.ApplicationValidationException;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:29
 */
@Getter
@Embeddable
public class MobileNumber implements Serializable {
    private String value;


    public MobileNumber(String value) {
        if (!isMobilePhoneNumber(value)) {
            throw new ApplicationValidationException(ApplicationValidationException.INVALID_REQUEST_DATA, "Invalid mobile phone number.");
        }
        this.value = value;
    }

    protected MobileNumber() {
        this.value = "";
    }

    private boolean isMobilePhoneNumber(String value) {
        String mobilePhoneNumberPattern =  "^(\\+86)?[1][3|4|5|7|8|9][0-9]{9}$";
        return Pattern.compile(mobilePhoneNumberPattern).matcher(value).matches();
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {return value;}

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o instanceof MobileNumber that) {
            return this.value.equals(that.value);
        }
        if (o instanceof String str) {
            return this.value.equals(str);
        }
        return false;
    }
}
