package com.stardata.starshop2.authcontext.domain;

import com.stardata.starshop2.authcontext.domain.loginlog.LoginLog;
import com.stardata.starshop2.authcontext.domain.user.User;
import com.stardata.starshop2.authcontext.south.port.LoginLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/23 23:23
 */
@Service
@AllArgsConstructor
public class LoginLogService {
    private final LoginLogRepository logRepository;
    public LoginLog recordLogin(User user, String loginIp) {
        LoginLog log = LoginLog.createFor(user, loginIp);
        logRepository.add(log);
        return log;
    }
}
