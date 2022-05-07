package com.stardata.starshop2.productcontext.command.domain;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.productcontext.command.domain.product.Product;
import com.stardata.starshop2.productcontext.command.south.port.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/6 13:09
 */
@Service
@AllArgsConstructor
public class ProductManagingService {
    private  final ProductRepository repository;

    public Product detail(LongIdentity productId) {
        return repository.instanceOf(productId);
    }
}
