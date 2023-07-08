package com.stardata.starshop2.pl;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/7 19:28
 */
@Data
public class CustomerInfoDto {
    private Long id;

    private Long shopId;

    //归属用户ID
    private Long userId;

    //头像
    private String avatarUrl;

    private String name;

    /**
     * 1：男；2：女；0：未知；
     */
    private Character gender;

    private String mobilePhone;

    private String level;

    private LocalDate birthday;

    private String country;

    private String province;

    private String city;

    private String district;

    private List<String> labels;

    private String memo;
}
