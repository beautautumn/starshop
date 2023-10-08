package com.stardata.starshop2.api;

import com.stardata.starshop2.pl.CustomerInfoDto;
import com.stardata.starshop2.pl.UserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Samson Shu
 * @email shush@stardata.top
 * @date  2023/7/7 19:38
 * @version 1.0
 */
@FeignClient(value = "customer-biz-services")
public interface CustomerBizService {
     @PutMapping("/v2/shops/{shopId}/customers/corresponding_to_user")
     CustomerInfoDto ensureUserCustomerInfo(@RequestBody UserInfoDto userInfo, @PathVariable Long shopId);
}
