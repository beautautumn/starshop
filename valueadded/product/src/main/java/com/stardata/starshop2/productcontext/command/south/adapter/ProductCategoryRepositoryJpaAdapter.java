package com.stardata.starshop2.productcontext.command.south.adapter;

import com.stardata.starshop2.productcontext.command.domain.productcategory.ProductCategory;
import com.stardata.starshop2.productcontext.command.south.port.ProductCategoryRepository;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.south.adapter.GenericRepository;
import org.springframework.stereotype.Component;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/19 13:59
 */
@Adapter(PortType.Repository)
@Component
public class ProductCategoryRepositoryJpaAdapter implements ProductCategoryRepository {
    private final GenericRepository<ProductCategory, LongIdentity> repository;

    public ProductCategoryRepositoryJpaAdapter(GenericRepository<ProductCategory, LongIdentity> repository) {
        this.repository = repository;
    }

    @Override
    public ProductCategory instanceOf(LongIdentity categoryId) {
        return repository.findById(categoryId);
    }

    @Override
    public void update(ProductCategory category) {
        this.repository.saveOrUpdate(category);
    }

    @Override
    public void add(ProductCategory category) {
        this.repository.saveOrUpdate(category);
    }
}
