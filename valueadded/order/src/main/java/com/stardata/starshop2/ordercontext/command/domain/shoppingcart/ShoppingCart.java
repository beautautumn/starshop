package com.stardata.starshop2.ordercontext.command.domain.shoppingcart;

import com.stardata.starshop2.sharedcontext.domain.AbstractEntity;
import com.stardata.starshop2.sharedcontext.domain.AggregateRoot;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ShoppingCart extends AbstractEntity<LongIdentity>  implements AggregateRoot<ShoppingCart> {
    private LongIdentity id;

    @Override
    public LongIdentity id() {
        return this.id;
    }

    @Override
    public ShoppingCart root() {
        return this;
    }
}
