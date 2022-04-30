package com.stardata.starshop2.foundation.authcontext.domain.user;

import com.stardata.starshop2.sharedcontext.domain.AbstractEntity;
import com.stardata.starshop2.sharedcontext.domain.AggregateRoot;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.domain.MobileNumber;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/2/24 21:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class User extends AbstractEntity<LongIdentity> implements AggregateRoot<User>{
    private LongIdentity id;
    private String avatarUrl;
    private String country;
    private String province;
    private String city;
    private String language;
    private Integer gender;
    private String nickName;
    private WxOpenId openId;
    private UserToken token;

    User(String nickName, Integer gender) {
        this.nickName = nickName;
        this.gender = gender;
        this.id = null;
    }

    public static User of(String nickName, Integer gender) {
       return new User(nickName, gender);
    }

    @Override
    public User root() {
        return this;
    }

    @Override
    public LongIdentity id() {
        return this.id;
    }

    public User avatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public User country(String country) {
        this.country = country;
        return this;
    }

    public User province(String province) {
        this.province = province;
        return this;
    }

    public User city(String city) {
        this.city = city;
        return this;
    }

    public User language(String language) {
        this.language = language;
        return this;
    }

    public void refreshLoginToken(String sessionKey) {
        //todo 完成用户刷新登录令牌方法
    }

    public void copyInfoFrom(User loginUser) {
        //todo 完成复制用户信息方法
    }

    public void updateMobileNumber(MobileNumber mobileNumber) {
        //todo 完成更新用户手机号方法
    }
}
