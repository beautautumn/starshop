package com.stardata.starshop2.customercontext.north.remote;

import com.stardata.starshop2.api.CustomerBizService;
import com.stardata.starshop2.customercontext.north.local.CustomerAppService;
import com.stardata.starshop2.pl.CustomerInfoDto;
import com.stardata.starshop2.pl.UserInfoDto;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.jetbrains.annotations.NotNull;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/8 20:19
 */
@DubboService
@AllArgsConstructor
public class CustomerDubboService implements CustomerBizService {
    private final CustomerAppService appService;

    @Override
    public CustomerInfoDto ensureUserCustomerInfo(@NotNull UserInfoDto userInfo, @NotNull Long shopId) {
        return appService.ensureCustomerByUser(userInfo, shopId);
    }
}
