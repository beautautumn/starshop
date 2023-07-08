package com.stardata.starshop2.api;

import com.stardata.starshop2.pl.UserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Samson Shu
 * @email shush@stardata.top
 * @date  2023/7/7 19:38
 * @version 1.0
 */
@FeignClient(value = "user-biz-services")
public interface UserBizService {
     @GetMapping("/v2/users/{userId}")
     UserInfoDto getUserInfo(@PathVariable Long userId);
}
