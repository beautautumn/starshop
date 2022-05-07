package com.stardata.starshop2.authcontext.domain;

import com.stardata.starshop2.authcontext.domain.user.User;
import com.stardata.starshop2.authcontext.south.port.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/23 23:22
 */
@Service
@AllArgsConstructor
public class UserTokenService {
    private final UserRepository userRepository;

    public void generateLoginToken(User user, String sessionKey) {
        user.refreshLoginToken(sessionKey);
        userRepository.update(user);
    }
}
