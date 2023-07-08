package com.stardata.starshop2.authcontext.north.remote;

import com.stardata.starshop2.api.UserBizService;
import com.stardata.starshop2.authcontext.north.local.UserAppService;
import com.stardata.starshop2.pl.UserInfoDto;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.jetbrains.annotations.NotNull;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/9 00:18
 */
@DubboService
@AllArgsConstructor
public class UserDubboService implements UserBizService {
    private final UserAppService userAppService;

    @Override
    public UserInfoDto getUserInfo(@NotNull Long userId) {
        return userAppService.getUserInfoDto(userId);
    }
}
