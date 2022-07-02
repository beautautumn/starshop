package com.stardata.starshop2.sharedcontext.domain;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:15
 */
@Entity
@Getter
@Table(name = "tb_sys_parameter")
public class BizParameter extends AbstractEntity<StringIdentity> implements AggregateRoot<BizParameter> {
    @Transient
    public static final BizParameter EMPTY = new BizParameter();

    @EmbeddedId
    private StringIdentity code = StringIdentity.from("NONE");
    private String value= "";

    public int toInteger() {
        return Integer.parseInt(this.value);
    }
    public int toInteger(int defaultValue) {
        try {
            return Integer.parseInt(this.value);
        }catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public StringIdentity id() {
        return this.code;
    }

    @Override
    public BizParameter root() {
        return this;
    }
}
