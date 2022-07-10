package com.stardata.starshop2.ordercontext.command;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.util.SignUtils;
import com.stardata.starshop2.ordercontext.command.domain.OrderManagingService;
import com.stardata.starshop2.ordercontext.command.domain.PrepayRequestGenerationService;
import com.stardata.starshop2.ordercontext.command.domain.order.*;
import com.stardata.starshop2.ordercontext.command.north.local.OrderAppService;
import com.stardata.starshop2.ordercontext.command.pl.OrderPayResultRequest;
import com.stardata.starshop2.ordercontext.command.pl.OrderResponse;
import com.stardata.starshop2.ordercontext.command.pl.OrderSubmitRequest;
import com.stardata.starshop2.ordercontext.command.pl.PrepayOrderResponse;
import com.stardata.starshop2.ordercontext.command.south.port.OrderItemsSettlementClient;
import com.stardata.starshop2.ordercontext.command.south.port.OrderRepository;
import com.stardata.starshop2.ordercontext.command.south.port.PrepayingClient;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.domain.SessionUser;
import com.thoughtworks.xstream.XStream;
import me.chanjar.weixin.common.util.xml.XStreamInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
     * 按照先聚合再端口、先原子再组合、从内向外的分解步骤。
     * 1. 测试南向网关客户端接口：结算订单商品，包括测试用例有：
     * 1.1 创建订单，其中有的商品是可售的、有的商品不可售，要求订单结算结果正确
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
     * 按照先聚合再端口、先原子再组合、从内向外的分解步骤。
     * 2. 测试订单聚合生成订单支付，测试用例包括：
     * 2.1 创建订单，其中有的商品是可售的、有的商品不可售，要求订单结算结果正确
         * 根据已经插入的商品数据，可以知道：
         * productId: 1，单价：10元，可售，无折扣
         * productId: 2，单价：20元，可售，固定价格优惠9.9元，限购1份
         * productId: 3，单价：30元，可售，0.9折销售，限购10份
         * productId: 4，单价：46元，不可售（售罄）
     */

    @Autowired
    PrepayRequestGenerationService prepayRequestGenerationService;

    // 2.1. 创建订单，其中有的商品是可售的、有的商品不可售，要求订单结算结果正确
    @Test
    @Transactional
    @Rollback(true)
    void should_create_payment_correctly_for_given_order() {
        //given: 创建订单，其中订单项包含的商品均可售
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity userId = LongIdentity.from(1L);
        Order order = Order.createFor(shopId, userId);
        SessionUser user = SessionUser.from(userId.value(), "o9Nvx4gUq9dfO1KQy7LL-gXS_EkI");
        
        order.addItem(LongIdentity.from(1L), 3);
        order.addItem(LongIdentity.from(2L), 1);
        order.addItem(LongIdentity.from(3L), 4);

        //when: 结算商品价格、并创建订单支付
        settlementClient.settleProducts(order);
        String requestMessage = prepayRequestGenerationService.generatePrepay(user, order);
        order.createPayment(requestMessage);

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
     * 按照先聚合再端口、先原子再组合、从内向外的分解步骤。
     * 3. 测试订单聚合生成订单操作日志，相关测试用例包括：
     * 3.1. 创建订单，然后针对订单创建操作日志
     */

    //3.1 创建订单，然后针对订单创建操作日志
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
     * 按照先聚合再端口、先原子再组合、从内向外的分解步骤。
     * 4. 测试订单聚合的持久化，测试用例包括：
     * 4.1 创建订单，然后完成商品价格计算、创建订单支付、创建操作日志后保存，再查询重建后信息正确
     */

    //4.1 创建订单，然后针对订单创建操作日志
    @Test
    @Transactional
    @Rollback(true)
    void should_save_and_load_order_correctly_by_given_order_after_settle_and_payment_and_operlog() {
        //given: 创建订单，其中订单项包含的商品均可售
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity userId = LongIdentity.from(1L);
        Order order = Order.createFor(shopId, userId);
        SessionUser user = SessionUser.from(userId.value(), "o9Nvx4gUq9dfO1KQy7LL-gXS_EkI");

        order.addItem(LongIdentity.from(1L), 3);
        order.addItem(LongIdentity.from(2L), 1);
        order.addItem(LongIdentity.from(3L), 4);

        //when: 为订单创建操作日志并保存
        settlementClient.settleProducts(order);
        String requestMessage = prepayRequestGenerationService.generatePrepay(user, order);
        order.createPayment(requestMessage);
        order.recordOperLog(userId, OrderOperType.CREATE, "创建新订单测试");
        orderRepository.add(order);
        entityManager.flush();

        //then: 检查订单金额、订单支付、操作日志创建成功且内容正确
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

    @Autowired
    OrderManagingService orderManagingService;

    /**
     * 任务级测试：创建付款订单——提交新订单；（组合任务，领域服务）
     * 按照先聚合再端口、先原子再组合、从内向外的分解步骤。
     * 5. 测试领域服务提交新订单，相关测试用例包括：
     * 5.1 领域服务提交付款订单
     */

    // 5.1 领域服务提交付款订单
    @Test
    @Transactional
    @Rollback(true)
    void should_create_order_for_paying_correctly_by_domain_service() {
        //given: 创建订单，其中订单项包含的商品均可售
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity userId = LongIdentity.from(1L);
        Order order = Order.createFor(shopId, userId);
        SessionUser user = SessionUser.from(userId.value(), "o9Nvx4gUq9dfO1KQy7LL-gXS_EkI");

        order.addItem(LongIdentity.from(1L), 3);
        order.addItem(LongIdentity.from(2L), 2);
        order.addItem(LongIdentity.from(3L), 12);

        //when: 调用领域服务提交订单
        orderManagingService.submitOrder(user, order);
        entityManager.flush();

        //then: 检查订单金额、订单支付、操作日志创建成功且内容正确
        Order loadedOrder = orderRepository.instanceOf(order.getId());

        assertEquals(loadedOrder.getTotalAmountFen(), 3*1000+990+10*9*300);
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


    @Autowired
    OrderAppService orderAppService;

    /**
     * 服务级测试：创建付款订单——创建付款订单；（组合任务，应用服务）
     * 6. 测试订单应用服务创建订单接口，测试用例包括：
     * 6.1 应用服务创建订单
     */

    //6.1 应用服务创建订单
    @Test
    @Transactional
    @Rollback(true)
    void should_create_order_succeed_by_given_request_for_login_user_and_shopId() {
        //given: 准备登录用户、店铺id、订单提交请求
        Long shopId = 1L;
        SessionUser loginUser = SessionUser.from(1L, "o9Nvx4gUq9dfO1KQy7LL-gXS_EkI");
        OrderSubmitRequest request = new OrderSubmitRequest();
        request.item(1L, 3)
                .item(2, 2)
                .item(3, 12);


        //when: 调用订单应用服务的创建订单接口
        OrderResponse response = orderAppService.create(loginUser, shopId, request);

        //then: 订单创建成功，服务响应内容符合要求
        assertNotNull(response);
        assertEquals(response.getTotalAmountFen(), 3*1000+990+10*9*300);

        List<OrderResponse.Item> items = response.getItems();
        assertEquals(items.size(), 3);

        OrderResponse.Item item1 = items.get(0);
        assertEquals(item1.productId(), 1L);
        assertEquals(item1.count(), 3);
        assertEquals(item1.amountFen(), 3000L);

        OrderResponse.Item item2 = items.get(1);
        assertEquals(item2.productId(), 2L);
        assertEquals(item2.count(), 1);
        assertEquals(item2.amountFen(), 990L);

        OrderResponse.Item item3 = items.get(2);
        assertEquals(item3.productId(), 3L);
        assertEquals(item3.count(), 10);
        assertEquals(item3.amountFen(), 27000L);
    }

    /**
     * 任务级测试：发起订单预支付；——发起订单预支付；（原子任务，客户端端口，访问微信支付接口）
     * 按照先聚合再端口、先原子再组合、从内向外的分解步骤。
     * 7. 测试微信支付客户端预支付接口
     * 7.1 根据从数据库重建的订单聚合，调用微信支付客户端端口，发起支付
     */

    @Autowired
    PrepayingClient prepayingClient;

    //7.1 根据从数据库重建的订单聚合，调用微信支付客户端端口，发起支付
    @Test
    @Transactional
    @Rollback(true)
    void should_prepay_succeed_with_client_port_by_given_order() {
        //given: 使用领域服务先提交一个订单（准备测试数据）
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity userId = LongIdentity.from(1L);
        Order order = Order.createFor(shopId, userId);
        SessionUser user = SessionUser.from(userId.value(), "oVsAw5cdcnIxaae-x98ShoH93Hu0");

        order.addItem(LongIdentity.from(1L), 3);
        order.addItem(LongIdentity.from(2L), 2);
        order.addItem(LongIdentity.from(3L), 12);

        orderManagingService.submitOrder(user, order);
        entityManager.flush();

        //when: 调用预支付客户端端口进行预支付
        PrepayOrder prepay = prepayingClient.prepay(order.getPayment());

        orderRepository.update(order);
        entityManager.flush();

        //then: 检查预支付是否成功
        OrderPayment payment = order.getPayment();
        assertNotNull(payment);
        assertNotNull(payment.getPrepayId());

        assertNotNull(prepay);
        assertEquals(prepay.getOrderId(), order.getId());
        assertEquals(prepay.getPrepayId(), payment.getPrepayId());
        assertNotNull(prepay.getAppId());
        assertNotNull(prepay.getAppResult());
    }

    /**
     * 任务级测试：发起订单预支付；——发起订单预支付；（组合任务，领域服务）
     * 按照先聚合再端口、先原子再组合、从内向外的分解步骤。
     * 8. 测试订单领域服务预支付功能
     * 8.1 调用订单领域服务 OrderManagingService.prepay，发起预支付
     */

    //8.1 调用订单领域服务 OrderManagingService.prepay，发起预支付
    @Test
    @Transactional
    @Rollback(true)
    void should_prepay_succeed_with_domain_serice_by_given_order() {
        //given: 使用领域服务先提交一个订单（准备测试数据）
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity userId = LongIdentity.from(1L);
        Order order = Order.createFor(shopId, userId);
        SessionUser user = SessionUser.from(userId.value(), "oVsAw5cdcnIxaae-x98ShoH93Hu0");

        order.addItem(LongIdentity.from(1L), 3);
        order.addItem(LongIdentity.from(2L), 2);
        order.addItem(LongIdentity.from(3L), 12);

        orderManagingService.submitOrder(user, order);
        entityManager.flush();

        //when: 调用订单领域服务 OrderManagingService.prepay
        PrepayOrder prepay = orderManagingService.prepayOrder(order.getId());
        entityManager.flush();

        //then: 检查预支付是否成功
        OrderPayment payment = order.getPayment();
        assertNotNull(payment);
        assertNotNull(payment.getPrepayId());

        assertNotNull(prepay);
        assertEquals(prepay.getOrderId(), order.getId());
        assertEquals(prepay.getPrepayId(), payment.getPrepayId());
        assertNotNull(prepay.getAppId());
        assertNotNull(prepay.getAppResult());
    }

    /**
     * 服务级测试：发起订单预支付；（组合任务，应用服务）
     * 9. 测试订单应用服务发起订单预支付，测试用例包括：
     * 9.1 应用服务发起订单预支付，获取供前端app使用的返回参数
     */

    //9.1 应用服务发起订单预支付，获取供前端app使用的返回参数
    @Test
    @Transactional
    @Rollback(true)
    void should_prepay_succeed_with_app_serice_by_given_order() {
        //given: 使用领域服务先提交一个订单（准备测试数据）
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity userId = LongIdentity.from(1L);
        Order order = Order.createFor(shopId, userId);
        SessionUser user = SessionUser.from(userId.value(), "oVsAw5cdcnIxaae-x98ShoH93Hu0");

        order.addItem(LongIdentity.from(1L), 3);
        order.addItem(LongIdentity.from(2L), 2);
        order.addItem(LongIdentity.from(3L), 12);

        orderManagingService.submitOrder(user, order);
        entityManager.flush();

        //when: 调用订单应用服务预支付订单
        PrepayOrderResponse response = orderAppService.prepay(order.getId().value());

        //then: 检查预支付是否成功
        assertNotNull(response);
        assertNotNull(response.getPrepayId());
        assertNotNull(response.getAppResult());
    }

    /**
     * 任务级测试：生效订单——根据订单外部编号重建订单对象；（原子任务，资源库端口，访问数据库）
     * 按照先聚合再端口、先原子再组合、从内向外的分解步骤。
     * 10. 测试根据订单编号从数据库重建订单对象功能
     * 10.1 调用订单资源库服务，从数据库重建订单对象
     */
    @Test
    @Transactional
    @Rollback(true)
    void should_load_order_correctly_by_given_order_number() {
        //given: 使用领域服务先提交一个订单、并完成订单预支付（准备测试数据）
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity userId = LongIdentity.from(1L);
        Order order = Order.createFor(shopId, userId);
        SessionUser user = SessionUser.from(userId.value(), "oVsAw5cdcnIxaae-x98ShoH93Hu0");

        order.addItem(LongIdentity.from(1L), 3);
        order.addItem(LongIdentity.from(2L), 2);
        order.addItem(LongIdentity.from(3L), 12);

        orderManagingService.submitOrder(user, order);
        orderAppService.prepay(order.getId().value());
        entityManager.flush();

        //when: 根据订单外部编号, 调用订单资源库进行订单重建
        String orderNumber = order.getOrderNumber();
        Order loadedOrder = orderRepository.findByOutTradeNo(orderNumber);

        //then: 检查预支付是否成功
        assertNotNull(loadedOrder);
        assertNotNull(loadedOrder.getPayment().getPrepayId());
        assertEquals(loadedOrder.getUserId(), userId);

        assertEquals(loadedOrder.getTotalAmountFen(), 3*1000+990+10*9*300);

        List<OrderItem> items = loadedOrder.getItems();
        assertEquals(items.size(), 3);

        OrderItem item1 = items.get(0);
        assertEquals(item1.getProductId().value(), 1L);
        assertEquals(item1.getPurchaseCount(), 3);
        assertEquals(item1.getSubtotalFen(), 3000L);

        OrderItem item2 = items.get(1);
        assertEquals(item2.getProductId().value(), 2L);
        assertEquals(item2.getPurchaseCount(), 1);
        assertEquals(item2.getSubtotalFen(), 990L);

        OrderItem item3 = items.get(2);
        assertEquals(item3.getProductId().value(), 3L);
        assertEquals(item3.getPurchaseCount(), 10);
        assertEquals(item3.getSubtotalFen(), 27000L);
    }

    /**
     * 任务级测试：生效订单——生效订单；（组合任务，领域服务）
     * 按照先聚合再端口、先原子再组合、从内向外的分解步骤。
     * 10. 测试生效订单领域服务
     * 10.1 支付成功，调用领域服务，对指定外部编号的订单完成生效操作，标记订单支付成功、支付时间等，并记录相关支付结果信息
     * 10.2 支付失败，调用领域服务，对指定外部编号的订单完成生效操作，标记订单支付失败
     */

    // 10.1 支付成功，调用领域服务，对指定外部编号的订单完成生效操作，标记订单支付成功、支付时间等，并记录相关支付结果信息
    @Test
    @Transactional
    @Rollback(true)
    void should_make_order_effective_by_given_order_number_and_pay_succeed_info() {
        //given: 使用领域服务先提交一个订单、并完成订单预支付（准备测试数据）,并模拟一个微信支付通知消息
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity userId = LongIdentity.from(1L);
        Order order = Order.createFor(shopId, userId);
        SessionUser user = SessionUser.from(userId.value(), "oVsAw5cdcnIxaae-x98ShoH93Hu0");

        order.addItem(LongIdentity.from(1L), 3);
        order.addItem(LongIdentity.from(2L), 2);
        order.addItem(LongIdentity.from(3L), 12);

        orderManagingService.submitOrder(user, order);
        orderAppService.prepay(order.getId().value());
        entityManager.flush();

        //when: 根据订单外部编号, 调用订单资源库进行订单重建
        String orderNumber = order.getOrderNumber();
        PayResult payResult = PayResult.builder()
                .outTradeNo(orderNumber)
                .success(true)
                .payTime(LocalDateTime.now())
                .transactionId("testTransactionId")
                .cashFeeFen(order.getTotalAmountFen())
                .resultMessage("testResultMessage")
                .build();
        Order effectiveOrder = orderManagingService.makeOrderEffectively(payResult);

        //then: 检查订单是否生效成功
        assertNotNull(effectiveOrder);
        assertEquals(effectiveOrder.getStatus(), OrderStatus.PAID);
        assertEquals(effectiveOrder.getUserId(), userId);
        assertNotNull(effectiveOrder.getPayTime());
        OrderPayment payment = order.getPayment();
        assertNotNull(payment.getPrepayId());
        assertNotNull(payment.getTransactionId());
        assertEquals(payment.getCashFeeFen(), effectiveOrder.getTotalAmountFen());
        assertNotNull(payment.getResultMessage());
        assertNotNull(payment.getPayTime());
        assertEquals(payment.getStatus(), PaymentStatus.SUCCESS);
        assertNotNull(payment.getPayTime());

        boolean found = false;
        for (OrderOperLog operLog : effectiveOrder.getOperLogs()) {
            if (operLog.getOperType().equals(OrderOperType.PAY)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    // 10.2 支付失败，调用领域服务，对指定外部编号的订单完成生效操作，标记订单支付失败
    @Test
    @Transactional
    @Rollback(true)
    void should_make_order_not_effective_by_given_order_number_and_pay_failed_info() {
        //given: 使用领域服务先提交一个订单、完成订单预支付（准备测试数据）,并模拟一个支付成功结果
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity userId = LongIdentity.from(1L);
        Order order = Order.createFor(shopId, userId);
        SessionUser user = SessionUser.from(userId.value(), "oVsAw5cdcnIxaae-x98ShoH93Hu0");

        order.addItem(LongIdentity.from(1L), 3);
        order.addItem(LongIdentity.from(2L), 2);
        order.addItem(LongIdentity.from(3L), 12);

        orderManagingService.submitOrder(user, order);
        orderAppService.prepay(order.getId().value());
        entityManager.flush();

        //when: 根据订单外部编号, 调用订单资源库进行订单重建
        String orderNumber = order.getOrderNumber();
        PayResult payResult = PayResult.builder()
                .outTradeNo(orderNumber)
                .success(false)
                .build();
        Order effectiveOrder = orderManagingService.makeOrderEffectively(payResult);

        //then: 检查订单是否生效成功
        assertNotNull(effectiveOrder);
        assertEquals(effectiveOrder.getStatus(), OrderStatus.TO_PAY);
        assertEquals(effectiveOrder.getUserId(), userId);
        assertNull(effectiveOrder.getPayTime());
        OrderPayment payment = order.getPayment();
        assertNotNull(payment.getPrepayId());
        assertNull(payment.getTransactionId());
        assertNull(payment.getCashFeeFen());
        assertNull(payment.getResultMessage());
        assertNull(payment.getPayTime());
        assertEquals(payment.getStatus(), PaymentStatus.FAILED);
        assertNull(payment.getPayTime());

        boolean found = false;
        for (OrderOperLog operLog : effectiveOrder.getOperLogs()) {
            if (operLog.getOperType().equals(OrderOperType.PAY)) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }

    @Autowired
    private WxPayService wxPayService;

    /**
     * 服务级测试：生效订单——处理微信支付通知；（组合任务，应用服务）
     * 11. 测试订单应用服务处理微信支付通知接口，测试用例包括：
     * 11.1 处理微信支付通知消息成功，订单被标记为支付成功状态
     */
    @Test
    @Transactional
    @Rollback(true)
    void should_handle_wepay_notification_success()  {
        //given: 使用领域服务先提交一个订单、完成订单预支付（准备测试数据）,并模拟一个微信支付通知消息
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity userId = LongIdentity.from(1L);
        Order order = Order.createFor(shopId, userId);
        SessionUser user = SessionUser.from(userId.value(), "oVsAw5cdcnIxaae-x98ShoH93Hu0");

        order.addItem(LongIdentity.from(1L), 3);
        order.addItem(LongIdentity.from(2L), 2);
        order.addItem(LongIdentity.from(3L), 12);

        orderManagingService.submitOrder(user, order);
        orderAppService.prepay(order.getId().value());
        entityManager.flush();

        OrderPayment payment = order.getPayment();

        XStream xstream = XStreamInitializer.getInstance();
        xstream.processAnnotations(WxPayUnifiedOrderRequest.class);
        WxPayUnifiedOrderRequest prepayRequest =
                (WxPayUnifiedOrderRequest)xstream.fromXML(payment.getRequestMessage());
        WxPayOrderNotifyResult notifyResult = new WxPayOrderNotifyResult();
        notifyResult.setResultCode("SUCCESS");
        notifyResult.setReturnCode("SUCCESS");
        notifyResult.setAppid(prepayRequest.getAppid());
        notifyResult.setMchId(prepayRequest.getMchId());
        notifyResult.setDeviceInfo(prepayRequest.getDeviceInfo());
        notifyResult.setNonceStr(prepayRequest.getNonceStr());
        notifyResult.setSignType(prepayRequest.getSignType());
        notifyResult.setOpenid(prepayRequest.getOpenid());
        notifyResult.setTradeType(prepayRequest.getTradeType());
        notifyResult.setBankType("CMB_DEBIT");
        notifyResult.setTotalFee(prepayRequest.getTotalFee());
        notifyResult.setSettlementTotalFee(prepayRequest.getTotalFee());
        notifyResult.setFeeType("CNY");
        notifyResult.setCashFee(prepayRequest.getTotalFee());
        notifyResult.setCashFeeType("CNY");
        notifyResult.setTransactionId("1004400740201409030005092168");
        notifyResult.setOutTradeNo(payment.getOrder().getOrderNumber());
        notifyResult.setAttach(prepayRequest.getAttach());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        notifyResult.setTimeEnd(formatter.format(now));
        notifyResult.setVersion(prepayRequest.getVersion());
        String resultSign = SignUtils.createSign(notifyResult, prepayRequest.getSignType(),
                wxPayService.getConfig().getMchKey(), null);
        notifyResult.setSign(resultSign);


        //when: 调用OrderAppService
        String result = orderAppService.handleWxPayNotify(new OrderPayResultRequest(notifyResult));

        //then: 支付处理结果正确
        xstream = XStreamInitializer.getInstance();
        xstream.processAnnotations(WxPayNotifyResponse.class);
        WxPayNotifyResponse response = (WxPayNotifyResponse)xstream.fromXML(result);
        assertEquals(response.getReturnCode(), "SUCCESS");
        assertEquals(order.getStatus(), OrderStatus.PAID);
        assertEquals(order.getPayment().getCashFeeFen(), order.getTotalAmountFen());
        List<OrderOperLog> operLogs = order.getOperLogs();
        boolean found = false;
        for (OrderOperLog operLog : operLogs) {
            if (operLog.getOperType().equals(OrderOperType.PAY)) {
                found = true;
                break;
            }
        }
        assertTrue(found);

    }

}
