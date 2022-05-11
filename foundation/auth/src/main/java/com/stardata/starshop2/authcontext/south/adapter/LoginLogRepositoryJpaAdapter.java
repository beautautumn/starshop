package com.stardata.starshop2.authcontext.south.adapter;

import com.stardata.starshop2.authcontext.domain.loginlog.LoginLog;
import com.stardata.starshop2.authcontext.south.port.LoginLogRepository;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import org.springframework.stereotype.Repository;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/11 11:18
 */
@Adapter(PortType.Repository)
@Repository
public class LoginLogRepositoryJpaAdapter implements LoginLogRepository {
    @Override
    public void add(LoginLog loginLog) {

    }
}
