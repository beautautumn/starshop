package com.stardata.starshop2.ordercontext.command.domain.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stardata.starshop2.sharedcontext.domain.AbstractEntity;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.exception.InvalidFieldValueException;
import com.stardata.starshop2.sharedcontext.helper.Constants;
import com.stardata.starshop2.sharedcontext.helper.JSONUtil;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:28
 */
@Getter
@Entity
@Table(name="tb_order_item")
@SQLDelete(sql = "update tb_order set is_valid = '"+ Constants.DELETE_FLAG.DELETED+"' where id = ?")
@Where(clause = "is_valid = '"+ Constants.DELETE_FLAG.NORMAL+"'")
@AttributeOverrides({
        @AttributeOverride(name = "productId.id", column = @Column(name = "actual_product_id", nullable = false))
})
public class OrderItem  extends AbstractEntity<LongIdentity> {
    @EmbeddedId
    private LongIdentity id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @Embedded
    private LongIdentity productId;

    @Transient
    @Setter
    private String productName;

    @Setter
    private int purchaseCount;

    @Setter
    private long subtotalFen;

    private String productSnapshot;

    @Type(type = "com.stardata.starshop2.ordercontext.command.usertype.OrderItemStatusUserType")
    OrderItemStatus status;


    @Column(updatable = false)
    @CreationTimestamp
    LocalDateTime createTime;

    @UpdateTimestamp
    LocalDateTime updateTime;

    protected OrderItem(){}

    OrderItem(Order order, LongIdentity productId, int purchaseCount) {
        this.id = LongIdentity.snowflakeId();
        this.order = order;
        this.productId = productId;
        this.purchaseCount = purchaseCount;
        this.status = OrderItemStatus.TO_PICK;
    }

    @Override
    public LongIdentity id() {
        return this.id;
    }

    public void setProductSnapshot(String str) {
        try {
            JSONUtil.readTree(str);
            this.productSnapshot = str;
        } catch (JsonProcessingException e) {
            throw new InvalidFieldValueException("Product snapshot string must be valid json.");
        }
    }
}
