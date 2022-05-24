package com.stardata.starshop2.authcontext.domain;

import com.stardata.starshop2.authcontext.domain.user.User;
import com.stardata.starshop2.authcontext.domain.user.WxOpenId;
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
public class UserExistenceService {
    private final UserRepository userRepository;


    public User ensureUser(WxOpenId openId, User userToEnsure) {
        User user = userRepository.findByOpenId(openId);
        if (user == null) {
            userToEnsure.setOpenid(openId);
            user = userToEnsure;
            userRepository.add(user);
        } else {
            user.copyMiniAppInfoFrom(userToEnsure);
            userRepository.update(user);
        }
        return user;
    }
}
