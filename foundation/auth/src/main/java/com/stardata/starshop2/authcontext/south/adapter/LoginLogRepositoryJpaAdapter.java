package com.stardata.starshop2.authcontext.south.adapter;

import com.stardata.starshop2.authcontext.domain.loginlog.LoginLog;
import com.stardata.starshop2.authcontext.south.port.LoginLogRepository;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.south.adapter.GenericRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/11 11:18
 */
@Adapter(PortType.Repository)
@Component
@AllArgsConstructor
public class LoginLogRepositoryJpaAdapter implements LoginLogRepository {
    private final GenericRepository<LoginLog, LongIdentity> repository;

    @Override
    public void add(LoginLog loginLog) {
        this.repository.saveOrUpdate(loginLog);
    }

    @Override
    public LoginLog instanceOf(LongIdentity id) {
        return repository.findById(id);
    }
}
