package com.stardata.starshop2.foundation.authcontext.south.port;

import com.stardata.starshop2.foundation.authcontext.domain.user.User;
import com.stardata.starshop2.foundation.authcontext.domain.user.WxOpenId;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/2/24 21:10
 */
public interface UserRepository {
    User findByOpenId(WxOpenId openID);

    void add(User user);

    void update(User user);
}
