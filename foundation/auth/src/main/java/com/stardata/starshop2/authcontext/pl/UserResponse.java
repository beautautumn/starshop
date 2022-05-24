package com.stardata.starshop2.authcontext.pl;

import com.stardata.starshop2.authcontext.domain.user.User;
import lombok.Data;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/2/28 14:21
 */
@Data
public class UserResponse {
    private Long id;
    private String token;

    public static UserResponse from(User user) {
        return UserResponseMapper.INSTANCE.convert(user);
    }
}
