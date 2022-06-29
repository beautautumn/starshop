package com.stardata.starshop2.productcontext.command.usertype;

import com.stardata.starshop2.productcontext.command.domain.product.ProductDiscountType;
import com.stardata.starshop2.sharedcontext.usertype.PersistentEnumUserType;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/28 14:04
 */
public class ProductDiscountTypeUserType extends PersistentEnumUserType<ProductDiscountType> {

    @Override
    public Class<ProductDiscountType> returnedClass() {
        return ProductDiscountType.class;
    }
}
