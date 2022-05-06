package com.stardata.starshop2.valueadded.shopcontext.south.port;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.valueadded.shopcontext.domain.Shop;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/2/24 21:12
 */
public interface ShopRepository {
    Shop instanceOf(LongIdentity shopId) ;
}
