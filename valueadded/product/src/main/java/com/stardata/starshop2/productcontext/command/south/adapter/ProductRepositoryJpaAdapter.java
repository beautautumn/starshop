package com.stardata.starshop2.productcontext.command.south.adapter;

import com.stardata.starshop2.productcontext.command.domain.product.Product;
import com.stardata.starshop2.productcontext.command.south.port.ProductRepository;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.south.adapter.GenericRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/19 13:59
 */
@Adapter(PortType.Repository)
@Component
public class ProductRepositoryJpaAdapter implements ProductRepository {
    private final GenericRepository<Product, LongIdentity> repository;

    public ProductRepositoryJpaAdapter(GenericRepository<Product, LongIdentity> repository) {
        this.repository = repository;
    }

    @Override
    public Product instanceOf(LongIdentity productId) {
        return repository.findById(productId);
    }

    @Override
    public List<Product> instancesOf(Collection<LongIdentity> productIds) {
        /* 这里被重构掉了
        List<Product> result = new ArrayList<>();
        for (LongIdentity productId : productIds) {
            result.add(this.repository.findById(productId));
        }
        return result;
         */
        return repository.findByIds(productIds);
    }

    @Override
    public void update(Product product) {
        this.repository.saveOrUpdate(product);
    }

    @Override
    public void add(Product product) {
        this.repository.saveOrUpdate(product);
    }
}
