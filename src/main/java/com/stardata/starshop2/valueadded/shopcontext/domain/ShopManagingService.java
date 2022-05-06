package com.stardata.starshop2.valueadded.shopcontext.domain;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.valueadded.shopcontext.south.port.ShopRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/6 13:59
 */
@Service
@AllArgsConstructor
public class ShopManagingService {
    private final ShopRepository shopRepository;

    public Shop detail(LongIdentity shopId) {
        return shopRepository.instanceOf(shopId);
    }
}
