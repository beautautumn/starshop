package com.stardata.starshop2.ordercontext.command.domain.order;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import lombok.Getter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:30
 */
@Getter
@Entity
@Table(name="tb_order_oper_log")
@AttributeOverrides({
        @AttributeOverride(name = "userId.id", column = @Column(name = "user_id", nullable = false)),
})

public class OrderOperLog {
    @EmbeddedId
    LongIdentity id;

    @Embedded
    LongIdentity userId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    Order order;

    @Type(type="com.stardata.starshop2.ordercontext.command.usertype.OrderOperTypeUserType")
    OrderOperType operType;

    LocalDateTime operTime;
    String operDesc;

    protected OrderOperLog(){}

    protected OrderOperLog(Order order, LongIdentity userId, OrderOperType operType, String operDesc) {
        this.id = LongIdentity.snowflakeId();
        this.order = order;
        this.userId = userId;
        this.operType = operType;
        this.operDesc = operDesc;
        this.operTime = LocalDateTime.now();
    }


}
