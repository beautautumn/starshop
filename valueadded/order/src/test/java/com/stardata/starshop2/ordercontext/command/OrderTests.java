package com.stardata.starshop2.ordercontext.command;

import com.stardata.starshop2.ordercontext.command.domain.order.*;
import com.stardata.starshop2.ordercontext.command.south.port.OrderItemsSettlementClient;
import com.stardata.starshop2.ordercontext.command.south.port.OrderRepository;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/28 13:11
 */
@SpringBootTest
public class OrderTests {

    @Autowired
    OrderItemsSettlementClient settlementClient;

    /**
     * 任务级测试：创建付款订单——结算订单商品（含生成快照）
     * 按照先聚合再端口、先原子再组合、从内向外的原则。
     * 设计相关任务级测试案例包括：
     * 1.1. 创建订单，其中有的商品是可售的、有的商品不可售，要求订单结算结果正确
         * 根据已经插入的商品数据，可以知道：
         * productId: 1，单价：10元，可售，无折扣
         * productId: 2，单价：20元，可售，固定价格优惠9.9元，限购1份
         * productId: 3，单价：30元，可售，0.9折销售，限购10份
         * productId: 4，单价：46元，不可售（售罄）
     */

    @Test
    @Transactional
    @Rollback(true)
    // 1.1. 创建订单，其中有的商品是可售的、有的商品不可售，要求订单结算结果正确
    void should_settle_order_item_correctly() {
        //given: 创建订单，其中订单项包含的商品有的可售、有的售罄

        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity userId = LongIdentity.from(1L);
        Order order = Order.createFor(shopId, userId);


        order.addItem(LongIdentity.from(1L), 5);
        order.addItem(LongIdentity.from(2L), 5);
        order.addItem(LongIdentity.from(3L), 5);
        order.addItem(LongIdentity.from(4L), 5);

        //when: 调用settlementClient进行订单结算
        settlementClient.settleProducts(order);

        //then: 订单结算结果符合商品的可售、特价等政策
        OrderItem item1 = order.getItems().get(0);
        assertEquals(item1.getProductId().value(), 1L);
        assertEquals(item1.getPurchaseCount(), 5);
        assertEquals(item1.getSubtotalFen(), 5*1000L);
        assertTrue(item1.getProductSnapshot().length() > 0);

        OrderItem item2 = order.getItems().get(1);
        assertEquals(item2.getProductId().value(), 2L);
        assertEquals(item2.getPurchaseCount(), 1);
        assertEquals(item2.getSubtotalFen(), 990L);
        assertTrue(item2.getProductSnapshot().length() > 0);

        OrderItem item3 = order.getItems().get(2);
        assertEquals(item3.getProductId().value(), 3L);
        assertEquals(item3.getPurchaseCount(), 5);
        assertEquals(item3.getSubtotalFen(), 5*9*300L);
        assertTrue(item3.getProductSnapshot().length() > 0);

        OrderItem item4 = order.getItems().get(3);
        assertEquals(item4.getProductId().value(), 4L);
        assertEquals(item4.getPurchaseCount(), 0);
        assertEquals(item4.getSubtotalFen(), 0L);
        assertTrue(item4.getProductSnapshot().length() > 0);
    }

    /**
     * 任务级测试：创建付款订单——生成订单支付；（原子任务，聚合，实体对象行为）
     * 按照先聚合再端口、先原子再组合、从内向外的原则。
     * 设计相关任务级测试案例包括：
     * 2.1. 创建订单，其中有的商品是可售的、有的商品不可售，要求订单结算结果正确
         * 根据已经插入的商品数据，可以知道：
         * productId: 1，单价：10元，可售，无折扣
         * productId: 2，单价：20元，可售，固定价格优惠9.9元，限购1份
         * productId: 3，单价：30元，可售，0.9折销售，限购10份
         * productId: 4，单价：46元，不可售（售罄）
     */

    // 2.1. 创建订单，其中有的商品是可售的、有的商品不可售，要求订单结算结果正确
    @Test
    @Transactional
    @Rollback(true)
    void should_create_payment_correctly_for_given_order() {
        //given: 创建订单，其中订单项包含的商品均可售
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity userId = LongIdentity.from(1L);
        Order order = Order.createFor(shopId, userId);
        
        order.addItem(LongIdentity.from(1L), 3);
        order.addItem(LongIdentity.from(2L), 1);
        order.addItem(LongIdentity.from(3L), 4);

        //when: 结算商品价格、并创建订单支付
        settlementClient.settleProducts(order);
        order.createPayment();

        //then: 订单支付的金额等信息正确
        assertEquals(order.getTotalAmountFen(), 3*1000+990+4*9*300);
        OrderPayment payment = order.getPayment();
        assertNotNull(payment);
        assertEquals(payment.getUserId(), userId);
        assertEquals(payment.getStatus(), PaymentStatus.TO_PAY);
        assertEquals(payment.getPayType(), PaymentType.ORDER);
    }

    /**
     * 任务级测试：创建付款订单——生成订单操作日志；（原子任务，聚合，实体对象行为）
     * 按照先聚合再端口、先原子再组合、从内向外的原则。
     * 设计相关任务级测试案例包括：
     * 3.1. 创建订单，然后针对订单创建操作日志
     */
    @Test
    @Transactional
    @Rollback(true)
    void should_create_order_operation_log_correctly() {
        //given: 创建订单，其中订单项包含的商品均可售
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity userId = LongIdentity.from(1L);
        Order order = Order.createFor(shopId, userId);

        order.addItem(LongIdentity.from(1L), 3);
        order.addItem(LongIdentity.from(2L), 1);
        order.addItem(LongIdentity.from(3L), 4);

        //when: 为订单创建操作日志并保存
        order.recordOperLog(userId, OrderOperType.CREATE, "创建新订单测试");

        //then: 检查操作日志创建成功
        assertTrue(order.getOperLogs().size()>0);
        OrderOperLog operLog = order.getOperLogs().get(0);
        assertNotNull(operLog);
        assertEquals(operLog.getUserId(), userId);
        assertEquals(operLog.getOperType(), OrderOperType.CREATE);
    }

    @Autowired
    OrderRepository orderRepository;
    @Resource
    EntityManager entityManager;

    /**
     * 任务级测试：创建付款订单——新订单持久化；（原子任务，资源库端口，访问数据库）
     * 按照先聚合再端口、先原子再组合、从内向外的原则。
     * 设计相关任务级测试案例包括：
     * 3.1. 创建订单，然后完成商品价格计算、创建订单支付、创建操作日志后保存，再查询重建后信息正确
     */
    @Test
    @Transactional
    @Rollback(true)
    void should_save_and_load_order_correctly_by_given_order_after_settle_and_payment_and_operlog() {
        //given: 创建订单，其中订单项包含的商品均可售
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity userId = LongIdentity.from(1L);
        Order order = Order.createFor(shopId, userId);

        order.addItem(LongIdentity.from(1L), 3);
        order.addItem(LongIdentity.from(2L), 1);
        order.addItem(LongIdentity.from(3L), 4);

        //when: 为订单创建操作日志并保存
        settlementClient.settleProducts(order);
        order.createPayment();
        order.recordOperLog(userId, OrderOperType.CREATE, "创建新订单测试");
        orderRepository.add(order);
        entityManager.flush();

        //then: 检查操作日志创建成功
        Order loadedOrder = orderRepository.instanceOf(order.getId());

        assertEquals(loadedOrder.getTotalAmountFen(), 3*1000+990+4*9*300);
        OrderPayment payment = order.getPayment();
        assertNotNull(payment);
        assertEquals(payment.getUserId(), userId);
        assertEquals(payment.getStatus(), PaymentStatus.TO_PAY);
        assertEquals(payment.getPayType(), PaymentType.ORDER);

        assertTrue(loadedOrder.getOperLogs().size()>0);
        OrderOperLog operLog = loadedOrder.getOperLogs().get(0);
        assertNotNull(operLog);
        assertEquals(operLog.getUserId(), userId);
        assertEquals(operLog.getOperType(), OrderOperType.CREATE);
    }
}
