package com.stardata.starshop2.authcontext.north.remote;

import com.stardata.starshop2.api.UserBizService;
import com.stardata.starshop2.authcontext.north.local.UserAppService;
import com.stardata.starshop2.pl.UserInfoDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/9 00:18
 */
@RestController
@RequestMapping("/v2/users/")
@AllArgsConstructor
public class UserSpringCloudService implements UserBizService {
    private final UserAppService userAppService;

    @GetMapping("{userId}")
    @Override
    public UserInfoDto getUserInfo(@PathVariable Long userId) {
        return userAppService.getUserInfoDto(userId);
    }
}
