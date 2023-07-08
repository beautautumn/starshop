package com.stardata.starshop2.pl;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/8 12:49
 */
@Data
public class UserInfoDto {
    private Long id;

    /**
     * 头像图片URL
     */
    private String avatarUrl;

    /**
     * 所属国家
     */
    private String country;

    /**
     * 所属省份
     */
    private String province;

    /**
     * 所属城市
     */
    private String city;

    /**
     * 所属语言
     */
    private String language;

    /**
     * 性别： 0-未知；1-男；2-女
     */
    private Character gender;

    /**
     * 网络昵称
     */
    private String nickName;

    /**
     * 注册时间
     */
    private LocalDateTime registerTime;

    /**
     * 微信openid
     */
    private String openid;

    /**
     * 手机号码
     */
    private String mobileNumber;
}
