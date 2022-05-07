package com.stardata.starshop2.ordercontext.command.domain.order;

import com.stardata.starshop2.sharedcontext.domain.AbstractEntity;
import com.stardata.starshop2.sharedcontext.domain.AggregateRoot;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Order extends AbstractEntity<LongIdentity> implements AggregateRoot<Order> {
    private LongIdentity id;
    private String shopId;
    private String userId;
    private LocalDateTime createTime;
    private String customerName;
    private Long totalAmountFen;

    private OrderPayment payment;

    public void createPayment() {
        //todo 完成订单创建支付记录方法
    }

    public void recordOperLog(LongIdentity userId, OrderOperType create) {
        //todo 完成订单记录操作日志方法
    }

    @Override
    public LongIdentity id() {
        return id;
    }

    @Override
    public Order root() {
        return this;
    }

    public String getBriefDescription() {
        //todo 完成订单简要描述字段生成
        return null;
    }

    public void makeEffectively() {
        //todo 完成订单生效方法
    }

    public void close() {
        //todo 完成订单关闭方法
    }

    public void setInvisible() {
        //todo 完成订单设置不可见方法
    }

    public void cancel() {
        //todo 完成订单取消方法
    }
}
