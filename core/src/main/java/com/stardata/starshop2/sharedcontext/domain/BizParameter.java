package com.stardata.starshop2.sharedcontext.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    @EmbeddedId
    private StringIdentity code;
    private String value;

    public int toInteger() {
        return Integer.parseInt(this.value);
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
