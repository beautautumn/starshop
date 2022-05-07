package com.stardata.starshop2.sharedcontext.domain;

import lombok.Data;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:15
 */
@Data
public class BizParameter {
    private String key;
    private String value;
    public int toInteger() {
        return Integer.parseInt(value);
    }
}
