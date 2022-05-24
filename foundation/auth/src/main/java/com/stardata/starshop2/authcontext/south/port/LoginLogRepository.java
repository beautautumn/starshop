package com.stardata.starshop2.authcontext.south.port;

import com.stardata.starshop2.authcontext.domain.loginlog.LoginLog;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/23 23:23
 */
public interface LoginLogRepository {
    void add(LoginLog loginLog);

    LoginLog instanceOf(LongIdentity id);
}
