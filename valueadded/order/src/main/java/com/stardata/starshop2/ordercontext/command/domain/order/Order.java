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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
        @AttributeOverride(name = "userId.id", column = @Column(name = "user_id", nullable = false))
})

@Getter
public class Order extends AbstractEntity<LongIdentity> implements AggregateRoot<Order> {

    @EmbeddedId
    private LongIdentity id;

    //归属店铺ID
    @Embedded
    private LongIdentity shopId;

    //归属用户ID
    @Embedded
    private LongIdentity userId;


    /**
     * 订单类型
     * 1：店铺订单；2：店铺子订单（用于品牌商接收订单）；3：接龙订单；4：接龙子订单（用于品牌商接收订单）；
     */
    @Type(type="com.stardata.starshop2.ordercontext.command.usertype.OrderTypeUserType")
    private OrderType type;

    //订单总额（分）
    private Long totalAmountFen;

    //订单编号，规则：年月日（YYYYMMDD）+门店编号（4位）+该门店当天下单序号（4位）
    private String orderNumber;

    //订单付款时间
    private LocalDateTime payTime;

    //订单操作日志列表
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    List<OrderOperLog> operLogs = new ArrayList<>();

    /**
     * 订单状态
     * 1：待支付；2：已支付；3：已配货；4：已发货；5：已结束；6：已取消；
     */
    @Type(type="com.stardata.starshop2.ordercontext.command.usertype.OrderStatusUserType")
    private OrderStatus status;

    @Column(updatable = false)
    @CreationTimestamp
    LocalDateTime createTime;

    @UpdateTimestamp
    LocalDateTime updateTime;


    //订单支付记录
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "order_id")
    private OrderPayment payment;

    //订单项列表
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private final List<OrderItem> items = new ArrayList<>();

    @Override
    public LongIdentity id() {
        return id;
    }

    @Override
    public Order root() {
        return this;
    }

    protected Order() {}

    protected Order(LongIdentity shopId, LongIdentity userId, OrderType type, OrderStatus status) {
        this.id = LongIdentity.snowflakeId();
        this.shopId = shopId;
        this.userId = userId;
        this.orderNumber = generateOderNumber(shopId, userId);
        this.type = type;
        this.status = status;
        this.createTime = LocalDateTime.now();
    }

    private String generateOderNumber(LongIdentity shopId, LongIdentity userId) {
        Random random = new Random(shopId.value());
        long orderSeq = random.nextLong(1000, 9999);
        String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String shopIdStr = shopId.toString();
        String shopNumber = (shopIdStr.length() > 18)?
                shopIdStr.substring(shopIdStr.length()-18): String.format("%018d", shopId.value());
        return String.format("P%s%s%d", todayStr, shopNumber, orderSeq);
    }

    public static Order createFor(LongIdentity shopId, LongIdentity userId) {
        return new Order(shopId, userId, OrderType.SHOP, OrderStatus.TO_PAY);
    }

    public void settleItem(LongIdentity productId, String productName, int purchaseCount, long subtotalFen, String productSnapshot) {
        List<LongIdentity> itemIds = this.items.stream().map(OrderItem::getProductId).collect(Collectors.toList());
        int idx = itemIds.indexOf(productId);
        if (idx < 0 ) return ;

        OrderItem item = items.get(idx);
        item.setProductName(productName);
        item.setPurchaseCount(purchaseCount);
        item.setSubtotalFen(subtotalFen);
        item.setProductSnapshot(productSnapshot);

        this.totalAmountFen = this.items.stream().mapToLong(OrderItem::getSubtotalFen).sum();
    }

    public OrderItem addItem(LongIdentity productId, int purchaseCount) {
        OrderItem item = new OrderItem(this, productId, purchaseCount);
        this.items.add(item);
        return item;
    }

    public void createPayment(String platformRequestMessage) {
        this.payment = new OrderPayment(this, this.userId, PaymentType.ORDER, platformRequestMessage);
    }

    public void recordOperLog(LongIdentity userId, OrderOperType operType, String operDesc) {
        OrderOperLog operLog = new OrderOperLog(this, userId, operType, operDesc);
        this.operLogs.add(operLog);
    }

    public void makeEffective(PayResult payResult) {
        this.payment.setResultMessage(payResult.getResultMessage());
        if (payResult.isSuccess()) {
            this.status = OrderStatus.PAID;
            this.payTime = payResult.getPayTime();
            this.payment.setTransactionId(payResult.getTransactionId());
            this.payment.setCashFeeFen(payResult.getCashFeeFen());
            this.payment.setPayTime(payResult.getPayTime());
            this.payment.setStatus(PaymentStatus.SUCCESS);

            this.recordOperLog(this.userId, OrderOperType.PAY, null);
        } else {
            this.status = OrderStatus.TO_PAY;
            this.payment.setStatus(PaymentStatus.FAILED);
        }
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

    public String getBriefDescription() {
        //todo 完成订单简要描述字段生成
        return null;
    }


}
