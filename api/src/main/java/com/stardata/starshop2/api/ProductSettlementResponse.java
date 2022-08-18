package com.stardata.starshop2.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/4 11:46
 */
@Data
public class ProductSettlementResponse {
    @Getter
    @AllArgsConstructor
    public static class Item {
            private Long id;
            private String name;
            private Long priceFen;
            private Integer count;
            private BigDecimal quantity;
            private boolean available;
            private String productSnapshot;
    }

    private final List<Item> items = new ArrayList<>();

}
