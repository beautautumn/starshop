package com.stardata.starshop2.valueadded.productcontext.command.south.port;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.valueadded.productcontext.command.domain.product.Product;

import java.util.Collection;
import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/4 12:01
 */
public interface ProductRepository {
    List<Product> instancesOf(Collection<LongIdentity> productIds);

    void update(Product product);

    Product instanceOf(LongIdentity productId);
}
