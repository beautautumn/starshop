package com.stardata.starshop2.productcontext.command.domain.product;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:38
 */
public enum ProductUoM {
    JIN("斤"),
    GONGJIN("公斤"),
    LIANG("两"),
    JIAN("件"),
    KE("克"),
    PING("瓶");

    private final String name;

    ProductUoM(String name) {
        this.name = name;
    }


    public String toString() {
            return this.name;
        }

    private static final Map<String, ProductUoM> uomTypeMap = new HashMap<>();

    static {
        for (ProductUoM uom : ProductUoM.values()) {
            uomTypeMap.put(uom.name, uom);
        }
    }

    public static ProductUoM from(String name) {
        return uomTypeMap.get(name);
    }

}
