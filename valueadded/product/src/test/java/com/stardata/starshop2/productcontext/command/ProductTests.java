package com.stardata.starshop2.productcontext.command;

import com.stardata.starshop2.productcontext.command.domain.ProductSettlement;
import com.stardata.starshop2.productcontext.command.domain.ProductSettlementService;
import com.stardata.starshop2.productcontext.command.domain.product.Product;
import com.stardata.starshop2.productcontext.command.domain.product.ProductDiscount;
import com.stardata.starshop2.productcontext.command.domain.product.ProductUoM;
import com.stardata.starshop2.productcontext.command.domain.productcategory.ProductCategory;
import com.stardata.starshop2.productcontext.command.south.port.ProductCategoryRepository;
import com.stardata.starshop2.productcontext.command.south.port.ProductRepository;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.domain.NonNegativeDecimal;
import com.stardata.starshop2.sharedcontext.domain.NonNegativeInteger;
import com.stardata.starshop2.sharedcontext.domain.PriceFen;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/4 23:53
 */
@SpringBootTest
public class ProductTests {
    /**
     * 任务级测试：从数据库重建多个Product对象
     * 按照先聚合再端口、先原子再组合、从内向外的原则。
     * 设计相关任务级测试案例包括：
     * 1.1. 根据多个已有产品的id，从数据库正常重建对应的Product对象列表；
     * 1.2. 根据多个已有产品的id、其中有的id对应的产品不存在，从数据库正常重建那些已有产品id对应的Product对象列表；
     * 1.3. 根据多个已有产品的id，其中所有产品id不存在，从数据库重建的产品列表为空；
     */

    @Resource
    EntityManager entityManager;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductCategoryRepository categoryRepository;

    //1.1. 根据多个已有产品的id，从数据库正常重建对应的Product对象列表；
    @Test
    @Transactional
    @Rollback(true)
    void should_load_all_exists_products_by_ids() {
        //given: 先在数据库创建几个产品
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity categoryId = LongIdentity.from(1L);
        ProductCategory category = categoryRepository.instanceOf(categoryId);
        ProductUoM unit = ProductUoM.JIN;

        Product product1 = Product.from(shopId, category, "testProduct1", unit);
        Product product2 = Product.from(shopId, category, "testProduct2", unit);
        Product product3 = Product.from(shopId, category, "testProduct3", unit);

        productRepository.add(product1);
        productRepository.add(product2);
        productRepository.add(product3);
        entityManager.flush();

        List<LongIdentity> productIds = new ArrayList<>();
        productIds.add(product1.getId());
        productIds.add(product2.getId());
        productIds.add(product3.getId());

        //when: 重建这些已有id的产品对象列表
        List<Product> products = productRepository.instancesOf(productIds);

        //then: 产品重建成功，且包含所有id对应的产品对象
        List<LongIdentity> loadedProductIds = products.stream().map(Product::getId).collect(Collectors.toList());
        for (LongIdentity productId : productIds) {
            assertTrue(loadedProductIds.contains(productId));
        }
    }

    //1.2. 根据多个已有产品的id、其中有的id对应的产品不存在，从数据库正常重建那些已有产品id对应的Product对象列表；
    @Test
    @Transactional
    @Rollback(true)
    void should_load_some_exists_products_by_ids() {
        //given: 先在数据库创建几个产品
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity categoryId = LongIdentity.from(1L);
        ProductCategory category = categoryRepository.instanceOf(categoryId);
        ProductUoM unit = ProductUoM.JIN;

        Product product1 = Product.from(shopId, category, "testProduct1", unit);
        Product product2 = Product.from(shopId, category, "testProduct2", unit);
        Product product3 = Product.from(shopId, category, "testProduct3", unit);

        productRepository.add(product1);
        productRepository.add(product2);
        productRepository.add(product3);
        entityManager.flush();

        List<LongIdentity> productIds = new ArrayList<>();
        productIds.add(product1.getId());
        productIds.add(product2.getId());
        productIds.add(product3.getId());

        List<LongIdentity> notExistsIds = new ArrayList<>();
        notExistsIds.add(LongIdentity.snowflakeId());
        notExistsIds.add(LongIdentity.snowflakeId());

        //when: 重建这些已有id的产品对象列表
        List<LongIdentity> allIds = new ArrayList<>();
        allIds.addAll(productIds);
        allIds.addAll(notExistsIds);
        List<Product> products = productRepository.instancesOf(allIds);

        //then: 产品重建成功，且包含所有id对应的产品对象
        List<LongIdentity> loadedProductIds = products.stream().map(Product::getId).collect(Collectors.toList());
        for (LongIdentity productId : productIds) {
            assertTrue(loadedProductIds.contains(productId));
        }
        for (LongIdentity productId : notExistsIds) {
            assertFalse(loadedProductIds.contains(productId));
        }
    }

    //1.3. 根据多个已有产品的id，其中所有产品id不存在，从数据库重建的产品列表为空；
    @Test
    @Transactional
    @Rollback(true)
    void should_load_empty_products_by_not_exists_ids() {
        //given: 先在数据库创建几个产品
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity categoryId = LongIdentity.from(1L);
        ProductCategory category = categoryRepository.instanceOf(categoryId);
        ProductUoM unit = ProductUoM.JIN;

        Product product1 = Product.from(shopId, category, "testProduct1", unit);
        Product product2 = Product.from(shopId, category, "testProduct2", unit);
        Product product3 = Product.from(shopId, category, "testProduct3", unit);

        productRepository.add(product1);
        productRepository.add(product2);
        productRepository.add(product3);
        entityManager.flush();

        List<LongIdentity> productIds = new ArrayList<>();
        productIds.add(product1.getId());
        productIds.add(product2.getId());
        productIds.add(product3.getId());

        List<LongIdentity> notExistsIds = new ArrayList<>();
        notExistsIds.add(LongIdentity.snowflakeId());
        notExistsIds.add(LongIdentity.snowflakeId());

        //when: 重建这些已有id的产品对象列表
        List<Product> products = productRepository.instancesOf(notExistsIds);

        //then: 产品重建成功，且包含所有id对应的产品对象
        List<LongIdentity> loadedProductIds = products.stream().map(Product::getId).collect(Collectors.toList());
        for (LongIdentity productId : productIds) {
            assertFalse(loadedProductIds.contains(productId));
        }
        for (LongIdentity productId : notExistsIds) {
            assertFalse(loadedProductIds.contains(productId));
        }
    }

    /**
     * 任务级测试：Product实体对象计算结算价格
     * 按照先聚合再端口、先原子再组合、从内向外的原则。
     * 设计相关任务级测试案例包括：
     * 2.1. 正常商品，有货、且已经上架、没有优惠折扣、有或没有购买限制，计算结果符合要求：
         *  settlePriceFen = [下单数量 or 购买限量] * 原始价格
         *  orderCount = [下单数量 or 购买限量]
         *  settleQuantity = 商品最小购买量 * [下单数量 or 购买限量]
         *  available = true
     * 2.2. 正常商品，有货、且已经上架、有或没有购买限制、有优惠折扣（按比例折扣），计算结果符合要求；
         *  settlePriceFen = [下单数量 or 购买限量] * 原始价格 * 优惠比例
         *  orderCount = [下单数量 or 购买限量]
         *  settleQuantity = 商品最小购买量 * [下单数量 or 购买限量]
         *  available = true
     * 2.3. 正常商品，有货、且已经上架、有或没有购买限制、有优惠折扣（按固定价格），计算结果符合要求；
         *  settlePriceFen = [下单数量 or 购买限量] * 固定价格
         *  orderCount = [下单数量 or 购买限量]
         *  settleQuantity = 商品最小购买量 * [下单数量 or 购买限量]
         *  available = true
     * 2.4. 异常商品，无货和/或未上架，计算结果符合要求：
         *  settlePriceFen = 0
         *  orderCount = 0
         *  settleQuantity = 0
         *  available = false
     */

    //2.1. 正常商品，有货、且已经上架、没有优惠折扣、有或没有购买限制，计算结果符合要求
    @Test
    @Transactional
    @Rollback(true)
    void should_settle_price_correctly_for_available_and_on_shelves_normal_product() {
        //given: 正常商品，有货、且已经上架、没有优惠折扣、有或没有购买限制两种情况
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity categoryId = LongIdentity.from(1L);
        ProductCategory category = categoryRepository.instanceOf(categoryId);
        ProductUoM unit = ProductUoM.JIN;
        PriceFen originalPriceFen = new PriceFen(1210L);
        NonNegativeDecimal minPurchase = new NonNegativeDecimal("0.5");
        NonNegativeInteger purchaseLimit = new NonNegativeInteger(3);

        Product product1 = Product.from(shopId, category, "testProduct1", unit)
                .isAvailable(true)
                .onShelves(true)
                .originalPriceFen(originalPriceFen)
                .minPurchase(minPurchase);

        Product product2 = Product.from(shopId, category, "testProduct2", unit)
                .isAvailable(true)
                .onShelves(true)
                .originalPriceFen(originalPriceFen)
                .minPurchase(minPurchase)
                .purchaseLimit(purchaseLimit);

        //when: 进行价格结算
        int orderCount = 5;
        ProductSettlement settlement1 = product1.settlePrice(orderCount);
        ProductSettlement settlement2 = product2.settlePrice(orderCount);

        /* then: 满足前述注释描述的结果要求
         *  settlePriceFen = [下单数量 or 购买限量] * 原始价格
         *  orderCount = [下单数量 or 购买限量]
         *  settleQuantity = 商品最小购买量 * [下单数量 or 购买限量]
         *  available = true
         */

        /* 下面的代码被重构优化掉 (为 ProductSettlement 重载 equals 方法 ）
        assertEquals(, settlement1.settlePriceFen());
        assertEquals(, settlement1.orderCount());
        assertEquals(, settlement1.settleQuantity());
        assertTrue(settlement1.available());
         */

        assertEquals(settlement1, new ProductSettlement(
                product1.getId(),
                orderCount*originalPriceFen.value(),
                orderCount,
                minPurchase.value().multiply(BigDecimal.valueOf(orderCount)),
                true));


        /* 下面的代码被重构优化掉 (为 ProductSettlement 重载 equals 方法 ）
        assertEquals(purchaseLimit.value()*originalPriceFen.value(), settlement2.settlePriceFen());
        assertEquals(purchaseLimit.value(), settlement2.orderCount());
        assertEquals(minPurchase.value().multiply(BigDecimal.valueOf(purchaseLimit.value())), settlement2.settleQuantity());
        assertTrue(settlement2.available());
         */

        assertEquals(settlement2, new ProductSettlement(
                product2.getId(),
                purchaseLimit.value()*originalPriceFen.value(),
                purchaseLimit.value(),
                minPurchase.value().multiply(BigDecimal.valueOf(purchaseLimit.value())),
                true));
    }


    //2.2. 正常商品，有货、且已经上架、有或没有购买限制、有优惠折扣（按比例折扣），计算结果符合要求
    @Test
    @Transactional
    @Rollback(true)
    void should_settle_price_correctly_for_available_and_on_shelves_rate_discount_product() {
        //given: 正常商品，有货、且已经上架、有优惠折扣（按比例折扣）、有或没有购买限制两种情况
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity categoryId = LongIdentity.from(1L);
        ProductCategory category = categoryRepository.instanceOf(categoryId);
        ProductUoM unit = ProductUoM.JIN;
        PriceFen originalPriceFen = new PriceFen(1210L);
        NonNegativeDecimal minPurchase = new NonNegativeDecimal("0.5");
        NonNegativeInteger purchaseLimit = new NonNegativeInteger(3);

        NonNegativeDecimal discountRate = new NonNegativeDecimal("0.85");
        ProductDiscount discount = new ProductDiscount(discountRate);

        Product product1 = Product.from(shopId, category, "testProduct1", unit)
                .isAvailable(true)
                .onShelves(true)
                .originalPriceFen(originalPriceFen)
                .minPurchase(minPurchase)
                .discount(discount);

        Product product2 = Product.from(shopId, category, "testProduct2", unit)
                .isAvailable(true)
                .onShelves(true)
                .originalPriceFen(originalPriceFen)
                .minPurchase(minPurchase)
                .purchaseLimit(purchaseLimit)
                .discount(discount);

        //when: 进行价格结算
        int orderCount = 5;
        ProductSettlement settlement1 = product1.settlePrice(orderCount);
        ProductSettlement settlement2 = product2.settlePrice(orderCount);

        /* then: 满足前述注释描述的结果要求
         *  settlePriceFen = [下单数量 or 购买限量] * 原始价格 * 优惠比例
         *  orderCount = [下单数量 or 购买限量]
         *  settleQuantity = 商品最小购买量 * [下单数量 or 购买限量]
         *  available = true
         */

        assertEquals(settlement1, new ProductSettlement(
                product1.getId(),
                orderCount*originalPriceFen.multiply(discountRate).value(),
                orderCount,
                minPurchase.value().multiply(BigDecimal.valueOf(orderCount)),
                true));

        assertEquals(settlement2, new ProductSettlement(
                product2.getId(),
                purchaseLimit.value()*originalPriceFen.multiply(discountRate).value(),
                purchaseLimit.value(),
                minPurchase.value().multiply(BigDecimal.valueOf(purchaseLimit.value())),
                true));
    }

    //2.3. 正常商品，有货、且已经上架、有或没有购买限制、有优惠折扣（按固定价格），计算结果符合要求
    @Test
    @Transactional
    @Rollback(true)
    void should_settle_price_correctly_for_available_and_on_shelves_fixed_discount_product() {
        //given: 正常商品，有货、且已经上架、有优惠折扣（按固定价格）、有或没有购买限制两种情况
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity categoryId = LongIdentity.from(1L);
        ProductCategory category = categoryRepository.instanceOf(categoryId);
        ProductUoM unit = ProductUoM.JIN;
        PriceFen originalPriceFen = new PriceFen(1210L);
        NonNegativeDecimal minPurchase = new NonNegativeDecimal("0.5");
        NonNegativeInteger purchaseLimit = new NonNegativeInteger(3);

        PriceFen fixedPrice = new PriceFen(990L);
        ProductDiscount discount = new ProductDiscount(fixedPrice);

        Product product1 = Product.from(shopId, category, "testProduct1", unit)
                .isAvailable(true)
                .onShelves(true)
                .originalPriceFen(originalPriceFen)
                .minPurchase(minPurchase)
                .discount(discount);

        Product product2 = Product.from(shopId, category, "testProduct2", unit)
                .isAvailable(true)
                .onShelves(true)
                .originalPriceFen(originalPriceFen)
                .minPurchase(minPurchase)
                .purchaseLimit(purchaseLimit)
                .discount(discount);

        //when: 进行价格结算
        int orderCount = 5;
        ProductSettlement settlement1 = product1.settlePrice(orderCount);
        ProductSettlement settlement2 = product2.settlePrice(orderCount);

        /* then: 满足前述注释描述的结果要求
         *  settlePriceFen = [下单数量 or 购买限量] * 固定价格
         *  orderCount = [下单数量 or 购买限量]
         *  settleQuantity = 商品最小购买量 * [下单数量 or 购买限量]
         *  available = true
         */

        assertEquals(settlement1, new ProductSettlement(
                product1.getId(),
                orderCount*fixedPrice.value(),
                orderCount,
                minPurchase.value().multiply(BigDecimal.valueOf(orderCount)),
                true));

        assertEquals(settlement2, new ProductSettlement(
                product2.getId(),
                purchaseLimit.value()*fixedPrice.value(),
                purchaseLimit.value(),
                minPurchase.value().multiply(BigDecimal.valueOf(purchaseLimit.value())),
                true));
    }

    //2.4. 异常商品，无货和/或未上架，计算结果符合要求
    @Test
    @Transactional
    @Rollback(true)
    void should_settle_price_correctly_for_not_available_or_not_on_shelves_product() {
        //given: 异常商品，无货和/或未上架
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity categoryId = LongIdentity.from(1L);
        ProductCategory category = categoryRepository.instanceOf(categoryId);
        ProductUoM unit = ProductUoM.JIN;
        PriceFen originalPriceFen = new PriceFen(1210L);
        NonNegativeDecimal minPurchase = new NonNegativeDecimal("0.5");
        NonNegativeInteger purchaseLimit = new NonNegativeInteger(3);

        PriceFen fixedPrice = new PriceFen(990L);
        ProductDiscount discount = new ProductDiscount(fixedPrice);

        Product product1 = Product.from(shopId, category, "testProduct1", unit)
                .isAvailable(false)
                .onShelves(true)
                .originalPriceFen(originalPriceFen)
                .minPurchase(minPurchase)
                .discount(discount);

        Product product2 = Product.from(shopId, category, "testProduct2", unit)
                .isAvailable(true)
                .onShelves(false)
                .originalPriceFen(originalPriceFen)
                .minPurchase(minPurchase)
                .purchaseLimit(purchaseLimit)
                .discount(discount);

        Product product3 = Product.from(shopId, category, "testProduct3", unit)
                .isAvailable(false)
                .onShelves(false)
                .originalPriceFen(originalPriceFen)
                .minPurchase(minPurchase);

        //when: 进行价格结算
        int orderCount = 5;
        ProductSettlement settlement1 = product1.settlePrice(orderCount);
        ProductSettlement settlement2 = product2.settlePrice(orderCount);
        ProductSettlement settlement3 = product3.settlePrice(orderCount);

        /* then: 满足前述注释描述的结果要求
         *  settlePriceFen = 0
         *  orderCount = 0
         *  settleQuantity = 0
         *  available = false
         */

        ProductSettlement emptySettlement1 = new ProductSettlement(
                product1.getId(),0, 0,
                new BigDecimal("0.0"), false);
        assertEquals(settlement1, emptySettlement1);
        ProductSettlement emptySettlement2 = new ProductSettlement(
                product2.getId(),0, 0,
                new BigDecimal("0.0"), false);
        assertEquals(settlement2, emptySettlement2);
        ProductSettlement emptySettlement3 = new ProductSettlement(
                product3.getId(),0, 0,
                new BigDecimal("0.0"), false);
        assertEquals(settlement3, emptySettlement3);
    }

    /**
     * 服务级测试：对多个商品ID、下单数量进行结算
     * 按照先聚合再端口、先原子再组合、从内向外的原则。
     * 设计相关服务级测试案例包括：
     * 1.1. 根据多个已有产品的id，从数据库正常重建对应的Product对象列表；
     * 1.2. 根据多个已有产品的id、其中有的id对应的产品不存在，从数据库正常重建那些已有产品id对应的Product对象列表；
     * 1.3. 根据多个已有产品的id，其中所有产品id不存在，从数据库重建的产品列表为空；
     */
    @Autowired
    ProductSettlementService settlementService;

    @Test
    @Transactional
    @Rollback(true)
    void should_settle_price_correctly_for_given_product_ids_and_counts() {
        //given: 向数据库插入一些商品记录
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity categoryId = LongIdentity.from(1L);
        ProductCategory category = categoryRepository.instanceOf(categoryId);
        ProductUoM unit = ProductUoM.JIN;


        PriceFen originalPriceFen1 = new PriceFen(1000L);
        NonNegativeDecimal minPurchase1 = new NonNegativeDecimal("0.5");
        Product product1 = Product.from(shopId, category, "testProduct1", unit)
                .isAvailable(true)
                .onShelves(true)
                .originalPriceFen(originalPriceFen1)
                .minPurchase(minPurchase1);

        PriceFen fixedPrice = new PriceFen(990L);
        ProductDiscount discount = new ProductDiscount(fixedPrice);

        PriceFen originalPriceFen2 = new PriceFen(2000L);
        NonNegativeDecimal minPurchase2 = new NonNegativeDecimal("1");
        NonNegativeInteger purchaseLimit2 = new NonNegativeInteger(1);
        Product product2 = Product.from(shopId, category, "testProduct2", unit)
                .isAvailable(true)
                .onShelves(true)
                .originalPriceFen(originalPriceFen2)
                .minPurchase(minPurchase2)
                .purchaseLimit(purchaseLimit2)
                .discount(discount);

        PriceFen originalPriceFen3 = new PriceFen(3000L);
        NonNegativeDecimal minPurchase3 = new NonNegativeDecimal("1");
        NonNegativeInteger purchaseLimit3 = new NonNegativeInteger(10);
        Product product3 = Product.from(shopId, category, "testProduct3", unit)
                .isAvailable(true)
                .onShelves(true)
                .originalPriceFen(originalPriceFen3)
                .purchaseLimit(purchaseLimit3)
                .minPurchase(minPurchase3);

        productRepository.add(product1);
        productRepository.add(product2);
        productRepository.add(product3);
        entityManager.flush();

        //when: 调用 settlementService.calcSettlement 对商品购买列表进行结算
        Map<LongIdentity, Integer> productCountsMap = new HashMap<>();
        productCountsMap.put(product1.getId(), 5);
        productCountsMap.put(product2.getId(), 5);
        productCountsMap.put(product3.getId(), 5);
        List<ProductSettlement> settlements = settlementService.calcSettlement(productCountsMap);

        //then: 所有商品的结算总价正确、各商品结算数量和价格正确
        long totalPriceFen = settlements.stream().mapToLong(ProductSettlement::settlePriceFen).sum();
        assertEquals(totalPriceFen, (5000+990+15000));

        ProductSettlement settlement1 = new ProductSettlement(product1.getId(), 5000L, 5, new BigDecimal("2.5"), true);
        assertEquals(settlements.get(0), settlement1);
        ProductSettlement settlement2 = new ProductSettlement(product2.getId(),990L, 1, new BigDecimal("1.0"), true);
        assertEquals(settlements.get(1), settlement2);
        ProductSettlement settlement3 = new ProductSettlement(product3.getId(),15000L, 5, new BigDecimal("5.0"), true);
        assertEquals(settlements.get(2), settlement3);
    }
}
