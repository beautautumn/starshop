package com.stardata.starshop2.shopcontext.north.local;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.shopcontext.domain.Shop;
import com.stardata.starshop2.shopcontext.domain.ShopManagingService;
import com.stardata.starshop2.shopcontext.pl.ShopResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/2/23 00:36
 */
@Service
@AllArgsConstructor
public class ShopAppService {
    private final ShopManagingService managingService;

    public void initAlliance(String shopId) {

    }

    public ShopResponse getDetail(Long shopIdLong) {
        LongIdentity shopId = LongIdentity.from(shopIdLong);
        Shop shop = managingService.detail(shopId);
        return ShopResponse.from(shop);
    }
}
