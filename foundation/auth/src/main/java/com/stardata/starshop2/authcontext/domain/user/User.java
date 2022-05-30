package com.stardata.starshop2.authcontext.domain.user;

import com.stardata.starshop2.sharedcontext.domain.AbstractEntity;
import com.stardata.starshop2.sharedcontext.domain.AggregateRoot;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.domain.MobileNumber;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/2/24 21:06
 */
@Entity
@Getter
@Table(name = "tb_user")
public class User extends AbstractEntity<LongIdentity> implements AggregateRoot<User>{
    /**
     * 主键ID
     */
    @EmbeddedId
    private LongIdentity id;

    /**
     * 头像图片URL
     */
    @Setter
    @Column(name = "avatarurl")
    private String avatarUrl;

    /**
     * 所属国家
     */
    @Setter
    private String country;

    /**
     * 所属省份
     */
    @Setter
    private String province;

    /**
     * 所属城市
     */
    @Setter
    private String city;

    /**
     * 所属语言
     */
    @Setter
    private String language;

    /**
     * 性别： 0-未知；1-男；2-女
     */
    @Setter
    @Column(nullable = false)
    private Integer gender;

    /**
     * 网络昵称
     */
    @Setter
    @Column(name = "nickname", nullable = false)
    private String nickName;

    /**
     * 注册时间
     */
    @Column(nullable = false)
    private LocalDateTime registerTime;

    /**
     * 更新时间
     */
    @Column(nullable = false)
    private LocalDateTime updateTime;

    /**
     * 微信openid
     */
    @Setter
    @Embedded
    private WxOpenId openid;

    /**
     * 手机号码
     */
    @Embedded
    private MobileNumber mobileNumber;

    /**
     * 用户登录令牌
     * 由于实际上只有一个令牌生效，因此我们将这个LIST对象隐藏起来，只通过refreshLoginToken、currentToken向外提供服务
     */
    @ElementCollection
    @CollectionTable(name = "tb_user_token", joinColumns = @JoinColumn(name = "user_id"))
    @Getter(AccessLevel.NONE)
    private final List<UserToken> tokens = new ArrayList<>();

    /**
     * 构造子，包内可调用，确保创建后的用户对象可用
     * @param nickName 网络昵称
     * @param gender 性别
     */
    User(String nickName, Integer gender) {
        this.nickName = nickName;
        this.gender = gender;
        this.id = LongIdentity.snowflakeId();
        this.openid = WxOpenId.of("UNKNOWN");
        LocalDateTime now = LocalDateTime.now();
        this.registerTime = now;
        this.updateTime = now;
    }

    /**
     * 业务代码不要使用，这是给Hibernate、MapStruct使用的构造子
     */
    public User() {
        this( "UNKNOWN", 0);
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

    public UserToken refreshLoginToken(String sessionKey) {
        UserToken token = UserToken.generateForUser(this)
                .sessionKey(sessionKey);
        this.tokens.clear();
        this.tokens.add(token);
        return token;
    }

    public UserToken currentToken() {
        return this.tokens.size()>0? this.tokens.get(0): null;
    }

    public void copyMiniAppInfoFrom(User fromUser) {
        this.nickName = fromUser.nickName;
        this.gender = fromUser.gender;
        this.avatarUrl = fromUser.avatarUrl;
        this.country = fromUser.country;
        this.province = fromUser.province;
        this.city = fromUser.city;
        this.language = fromUser.language;
    }


    public void updateMobileNumber(MobileNumber mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! (o instanceof User user)) return false;
        return Objects.equals(id, user.id) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
