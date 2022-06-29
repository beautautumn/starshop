package com.stardata.starshop2.ordercontext.command.domain.order;

import com.stardata.starshop2.sharedcontext.domain.AbstractEntity;
import com.stardata.starshop2.sharedcontext.domain.AggregateRoot;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.helper.Constants;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:15
 */
@Entity
@Table(name="tb_order")
@SQLDelete(sql = "update tb_order set is_valid = '"+ Constants.DELETE_FLAG.DELETED+"' where id = ?")
@Where(clause = "is_valid = '"+ Constants.DELETE_FLAG.NORMAL+"'")
@AttributeOverrides({
        @AttributeOverride(name = "shopId.id", column = @Column(name = "shop_id", nullable = false)),
        @AttributeOverride(name = "userId.id", column = @Column(name = "user_id", nullable = false)),
        @AttributeOverride(name = "customerId.id", column = @Column(name = "cust_id", nullable = false)),
})

@Getter
public class Order extends AbstractEntity<LongIdentity> implements AggregateRoot<Order> {

    @EmbeddedId
    private LongIdentity id;

    @Embedded
    private LongIdentity shopId;

    @Embedded
    private LongIdentity userId;


    @Type(type="com.stardata.starshop2.ordercontext.command.usertype.OrderTypeUserType")
    private OrderType type;

    private Long totalAmountFen;

    private String orderNumber;

    @Type(type="com.stardata.starshop2.ordercontext.command.usertype.OrderStatusUserType")
    private OrderStatus status;

    @Column(updatable = false)
    @CreationTimestamp
    LocalDateTime createTime;

    @UpdateTimestamp
    LocalDateTime updateTime;


    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "order_id")
    private OrderPayment payment;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private final List<OrderItem> items = new ArrayList<>();


    public void createPayment() {
        this.payment = new OrderPayment(this.id, this.userId, PaymentType.ORDER);
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

    protected Order() {}

    protected Order(LongIdentity shopId, LongIdentity userId) {
        this.shopId = shopId;
        this.userId = userId;
        this.id = LongIdentity.snowflakeId();
    }

    public static Order createFor(LongIdentity shopId, LongIdentity userId) {
        return new Order(shopId, userId);
    }

    public void settleItem(LongIdentity productId, int purchaseCount, long subtotalFen, String productSnapshot) {
        List<LongIdentity> itemIds = this.items.stream().map(OrderItem::getProductId).collect(Collectors.toList());
        int idx = itemIds.indexOf(productId);
        if (idx < 0 ) return ;

        OrderItem item = items.get(idx);
        item.setPurchaseCount(purchaseCount);
        item.setSubtotalFen(subtotalFen);
        item.setProductSnapshot(productSnapshot);

        this.totalAmountFen = this.items.stream().mapToLong(OrderItem::getSubtotalFen).sum();
    }

    public OrderItem addItem(LongIdentity productId, int purchaseCount) {
        OrderItem item = new OrderItem(this.id, productId, purchaseCount);
        this.items.add(item);
        return item;
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
