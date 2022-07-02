package com.stardata.starshop2.productcontext.command.domain.product;

import com.stardata.starshop2.sharedcontext.exception.InvalidFieldValueException;
import jakarta.persistence.Embeddable;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:38
 */
@Embeddable
public class ProductName {
    private static int MAX_LENGTH = 60;

    private String name;

    ProductName(String name){
        if (!isValid(name)){
            throw new InvalidFieldValueException("Invalid product name.");
        }
        this.name = name;
    }

    private boolean isValid(String name) {
        //todo 设计商品名称的检测规则
        return StringUtils.isNotEmpty(name) && (name.length()<=MAX_LENGTH);
    }

    protected ProductName() {
        this.name ="UNKNOWN";
    }

    public static ProductName of(String name) {
        return new ProductName(name);
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
