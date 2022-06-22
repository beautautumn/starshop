package com.stardata.starshop2.productcontext.command.domain.product;

import jakarta.persistence.Embeddable;
import lombok.Getter;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:38
 * 之所以将产品描述包装为值对象，是为了支持将来具有格式化的产品描述能力
 */
@Embeddable
@Getter
public class ProductDescription {
    private String value;

    protected ProductDescription(){}

    public ProductDescription(String value) {
        this.value = value.substring(0, Math.min(value.length(), 500));
    }
}
