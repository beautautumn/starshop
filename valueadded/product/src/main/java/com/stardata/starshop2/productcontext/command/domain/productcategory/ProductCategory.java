package com.stardata.starshop2.productcontext.command.domain.productcategory;

import com.stardata.starshop2.sharedcontext.domain.AbstractEntity;
import com.stardata.starshop2.sharedcontext.domain.AggregateRoot;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:32
 */
@Getter
@Entity
@Table(name="tb_prod_category")
public class ProductCategory extends AbstractEntity<LongIdentity> implements AggregateRoot<ProductCategory> {
    @EmbeddedId
    private LongIdentity id;

    @AttributeOverride(name="id", column = @Column(name="shop_id", nullable = false))
    @Embedded
    private LongIdentity shopId;

    private String name;
    private Integer displayOrder;

    @Column(updatable = false)
    @CreationTimestamp
    LocalDateTime createTime;

    @UpdateTimestamp
    LocalDateTime updateTime;

    protected ProductCategory(){
        this(LongIdentity.from(0L), "DummyCategory");
    }

    public ProductCategory(LongIdentity shopId, String name) {
        this.id = LongIdentity.snowflakeId();
        this.shopId = shopId;
        this.name = name;
    }

    @Override
    public LongIdentity id() {
        return this.id;
    }

    @Override
    public ProductCategory root() {
        return this;
    }
}
