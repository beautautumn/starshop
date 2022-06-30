package com.stardata.starshop2.ordercontext.command.domain.order;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:16
 */
@Entity
@Table(name="tb_order_payment")
@AttributeOverrides({
        @AttributeOverride(name = "userId.id", column = @Column(name = "user_id", nullable = false)),
})

@Getter
public class OrderPayment {
    @EmbeddedId
    private LongIdentity id;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    Order order;

    @Embedded
    private LongIdentity userId;

    @Type(type="com.stardata.starshop2.ordercontext.command.usertype.PaymentTypeUserType")
    private PaymentType payType;

    @Type(type="com.stardata.starshop2.ordercontext.command.usertype.PaymentStatusUserType")
    private PaymentStatus status;

    private LocalDateTime payTime;

    @Column(updatable = false)
    @CreationTimestamp
    LocalDateTime createTime;

    @UpdateTimestamp
    LocalDateTime updateTime;

    protected OrderPayment(){}

    OrderPayment(Order order, LongIdentity userId, PaymentType payType) {
        this.id = LongIdentity.snowflakeId();
        this.order = order;
        this.userId = userId;
        this.payType = payType;
        this.status = PaymentStatus.TO_PAY;
    }
}
