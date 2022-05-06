package com.stardata.starshop2.valueadded.productcontext.command.domain.product;

import com.stardata.starshop2.sharedcontext.domain.AbstractEntity;
import com.stardata.starshop2.sharedcontext.domain.AggregateRoot;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Product  extends AbstractEntity<LongIdentity> implements AggregateRoot<Product> {
    private LongIdentity id;

    @Override
    public LongIdentity id() {
        return this.id;
    }

    @Override
    public Product root() {
        return this;
    }

    public ProductSettlement settlePrice(int count) {
        //todo 根据数量对商品进行结算，结算结果有：下单数量、结算数量、结算价格、是否有货
        return null;
    }

    public void increaseCurMonthSale(int count) {
        synchronized(this) {
            //todo 完成商品新增当月销量方法（要求线程安全）

        }
    }
}
