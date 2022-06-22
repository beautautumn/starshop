package com.stardata.starshop2.productcontext.command.south.port;

import com.stardata.starshop2.productcontext.command.domain.productcategory.ProductCategory;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/20 10:13
 */
public interface ProductCategoryRepository {
    ProductCategory instanceOf(LongIdentity categoryId);

    void update(ProductCategory category);

    void add(ProductCategory category);
}
