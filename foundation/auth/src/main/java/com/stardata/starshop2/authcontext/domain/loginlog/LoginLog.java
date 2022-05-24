package com.stardata.starshop2.authcontext.domain.loginlog;

import com.stardata.starshop2.authcontext.domain.user.User;
import com.stardata.starshop2.sharedcontext.domain.AbstractEntity;
import com.stardata.starshop2.sharedcontext.domain.AggregateRoot;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 14:54
 */
@Getter
@Entity
@Table(name="tb_login_log")
public class LoginLog extends AbstractEntity<LongIdentity> implements AggregateRoot<LoginLog> {
    @EmbeddedId
    private LongIdentity id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "id")
    })
    private User user;

    private LocalDateTime lastLoginTime;
    private String lastLoginIp;

    LoginLog(User user, String loginIp) {
        this.id = LongIdentity.snowflakeId();
        this.user = user;
        this.lastLoginIp = loginIp;
        this.lastLoginTime = LocalDateTime.now();
    }

    protected LoginLog() {
        this(User.of("UNKNOWN", 0), "0.0.0.0");
    }

    public static LoginLog createFor(User user, String loginIp) {
        return new LoginLog(user, loginIp);
    }

    @Override
    public LongIdentity id() {
        return this.id;
    }

    @Override
    public LoginLog root() {
        return this;
    }
}
