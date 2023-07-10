package com.stardata.starshop2.customercontext.north.remote;

import com.stardata.starshop2.api.CustomerBizService;
import com.stardata.starshop2.customercontext.north.local.CustomerAppService;
import com.stardata.starshop2.pl.CustomerInfoDto;
import com.stardata.starshop2.pl.UserInfoDto;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/8 20:19
 */
@RestController
@RequestMapping("/v2/shops/{shopId}/customers")
@AllArgsConstructor
public class CustomerSpringCloudService implements CustomerBizService {
    private final CustomerAppService appService;

    @PutMapping("/corresponding_to_user")
    @Override
    public CustomerInfoDto ensureUserCustomerInfo(@NotNull UserInfoDto userInfo, @PathVariable Long shopId) {
        return appService.ensureCustomerByUser(userInfo, shopId);
    }

    @GetMapping("/by_user")
    public CustomerInfoDto getCustomerByUserId(@RequestParam Long userId, @PathVariable String shopId) {
        return appService.getCustomerByUserId(userId);
    }
}
