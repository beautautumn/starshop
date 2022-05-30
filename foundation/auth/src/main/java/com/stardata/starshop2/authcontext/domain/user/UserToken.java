package com.stardata.starshop2.authcontext.domain.user;

import com.stardata.starshop2.sharedcontext.utils.CharUtil;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/24 00:09
 */
@Getter
@Embeddable
public class UserToken  implements Serializable {
    private final static long EXPIRE_SECONDS = 71*3600L;

    private String token;

    private String sessionKey;

    private LocalDateTime expireTime;

    private LocalDateTime updateTime;


    protected UserToken() {
        this.token = "";
    }

    public UserToken sessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
        return this;
    }

    public static UserToken generateForUser(User user) {
        LocalDateTime now = LocalDateTime.now();
        UserToken instance = new UserToken();
        instance.token = CharUtil.getRandomString(32);
        instance.updateTime = now;
        instance.expireTime = now.plusSeconds( EXPIRE_SECONDS );
        return instance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! (o instanceof UserToken that)) return false;
        return Objects.equals(this.token, that.token) &&
                Objects.equals(this.sessionKey, that.sessionKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.token + this.sessionKey);
    }

    @Override
    public String toString() {
        return String.format("token: %s, sessionKey: %s", this.token, this.sessionKey);
    }

}
