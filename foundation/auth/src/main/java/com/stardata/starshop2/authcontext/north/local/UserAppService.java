package com.stardata.starshop2.authcontext.north.local;

import com.stardata.starshop2.authcontext.domain.user.User;
import com.stardata.starshop2.authcontext.pl.UserInfoDtoMapper;
import com.stardata.starshop2.authcontext.south.port.UserRepository;
import com.stardata.starshop2.pl.UserInfoDto;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/9 00:23
 */
@Service
@AllArgsConstructor
public class UserAppService {
    private final UserRepository userRepository;

    public UserInfoDto getUserInfoDto(@NotNull Long userId) {
        User user = userRepository.instanceOf(LongIdentity.from(userId));
        if (user == null) return null;

        return UserInfoDtoMapper.INSTANCE.convert(user);
    }
}
