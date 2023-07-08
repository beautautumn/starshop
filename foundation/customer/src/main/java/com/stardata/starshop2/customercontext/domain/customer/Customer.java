package com.stardata.starshop2.customercontext.domain.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.stardata.starshop2.sharedcontext.domain.*;
import com.stardata.starshop2.sharedcontext.helper.Constants;
import com.stardata.starshop2.sharedcontext.helper.JSONUtil;
import com.stardata.starshop2.sharedcontext.helper.JsonAsStringDeserializer;
import lombok.*;
import org.hibernate.annotations.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/7 19:47
 */
@Entity
@Table(name="tb_customer")
@SQLDelete(sql = "update tb_customer set is_valid = '"+ Constants.DELETE_FLAG.DELETED+"' where id = ?")
@Where(clause = "is_valid = '"+ Constants.DELETE_FLAG.NORMAL+"'")
@AttributeOverrides({
        @AttributeOverride(name = "shopId.id", column = @Column(name = "shop_id", nullable = false)),
        @AttributeOverride(name = "userId.id", column = @Column(name = "user_id", nullable = false))
})
@Data
@EqualsAndHashCode(callSuper=false)
public class Customer  extends AbstractEntity<LongIdentity> implements AggregateRoot<Customer> {
    @EmbeddedId
    private LongIdentity id;

    //归属店铺ID
    @Embedded
    private LongIdentity shopId;

    //归属用户ID
    @Embedded
    private LongIdentity userId;

    //头像
    @Column(name="avatarUrl")
    @Setter(AccessLevel.NONE)
    private URL avatarUrl;

    private String name;

    /**
     * 1：男；2：女；0：未知；
     */
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    @Type(type = "com.stardata.starshop2.sharedcontext.usertype.GenderUserType")
    private Gender gender;

    @Embedded
    @AttributeOverride(name="value", column = @Column(name="mobile", length =20))
    private MobileNumber mobilePhone;

    private String level;

    private LocalDate birthday;

    private String country;

    private String province;

    private String city;

    private String district;

    /**
     * json字符串格式保存标签。例： ["忠实客户", "宝妈宝爸"]
     */
    @JsonDeserialize(using = JsonAsStringDeserializer.class)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String labels;

    private String memo;

    /**
     * 0：未读；1：已读。
     */
    private String hasRead;

    /**
     * 0：无效记录（被删除）；1：有效记录。
     */
    @JsonIgnore
    private String isValid;

    @JsonIgnore
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    @JsonIgnore
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updateTime;

    protected Customer() {}

    public static  Customer of(@NotNull LongIdentity shopId,
                               @NotNull LongIdentity userId,
                               @NotNull String name,
                               @NotNull MobileNumber mobilePhone,
                               @NotNull Gender gender){
        Customer customer = new Customer();
        customer.setName(name);
        customer.setShopId(shopId);
        customer.setUserId(userId);
        customer.setMobilePhone(mobilePhone);
        customer.gender = gender;
        return customer;
    }

    @Override
    public LongIdentity id() {
        return id;
    }

    @Override
    public Customer root() {
        return this;
    }

    public Customer avatarUrl(String avatarUrl) {
        try {
            this.avatarUrl = new URL(avatarUrl);
        } catch (MalformedURLException e) {
            this.avatarUrl = null;
        }
        return this;
    }

    public URL avatarUrl() {return this.avatarUrl;}

    public Customer country(String country) {
        this.country = country;
        return this;
    }

    public Customer province(String province) {
        this.province = province;
        return this;
    }

    public Customer city(String city) {
        this.city = city;
        return this;
    }

    public Customer labels(List<String> labels) {
        this.labels = JSONUtil.toJSONString(labels);
        return this;
    }

    public List<String> labels() {
        List<String> result = null;
        if (this.labels != null) {
            try {
                result = JSONUtil.parseList(this.labels, String.class);
            } catch (JsonProcessingException ignore) {}
        }
        return result;
    }


}
