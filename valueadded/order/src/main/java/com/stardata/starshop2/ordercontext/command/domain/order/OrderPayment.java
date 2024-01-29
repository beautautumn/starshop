package com.stardata.starshop2.ordercontext.command.domain.order;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
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
public class OrderPayment  implements Serializable {
    @EmbeddedId
    private LongIdentity id;

    @OneToOne
//    @JoinColumn(name = "order_id", referencedColumnName = "id")
    Order order;

    //支付记录归属用户ID
    @Embedded
    private LongIdentity userId;

    //订单支付类型
    @Type(type="com.stardata.starshop2.ordercontext.command.usertype.PaymentTypeUserType")
    private PaymentType payType;

    //支付记录状态
    @Type(type="com.stardata.starshop2.ordercontext.command.usertype.PaymentStatusUserType")
    @Setter
    private PaymentStatus status;

    //支付平台支付交易号
    @Setter
    private String prepayId;

    //支付平台支付交易号
    @Setter
    private String transactionId;

    //支付现金金额（分）
    @Setter
    private Long cashFeeFen;

    //支付平台请求报文
    private String requestMessage;

    //支付平台支付结果报文
    @Setter
    private String resultMessage;

    //支付时间
    @Setter
    private LocalDateTime payTime;

    @Column(updatable = false)
    @CreationTimestamp
    LocalDateTime createTime;

    @UpdateTimestamp
    LocalDateTime updateTime;

    protected OrderPayment(){}

    OrderPayment(Order order, LongIdentity userId, PaymentType payType, String requestMessage) {
        this.id = LongIdentity.snowflakeId();
        this.order = order;
        this.userId = userId;
        this.payType = payType;
        this.requestMessage = requestMessage;
        this.status = PaymentStatus.TO_PAY;
    }
}
