package com.stardata.starshop2.ordercontext.command.domain.shoppingcart;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:11
 */
public class ShoppingCartCategoryCount {
    private final Map<LongIdentity, Integer> counts = new HashMap<>();

    public int getCategoryCount(LongIdentity categoryId) {
        Integer count = this.counts.get(categoryId);
        return (count == null)? 0: count;
    }

    public void addCategoryCount(LongIdentity categoryId, int count) {
        Integer curCount = this.counts.get(categoryId);
        if (curCount != null) count += curCount;
        this.counts.put(categoryId, count);
    }

    public void clearCategoryCount(LongIdentity categoryId) {
        this.counts.remove(categoryId);
    }

    public void clearAll() {
        this.counts.clear();
    }
}
