package com.stardata.starshop2.ordercontext.command;

import com.stardata.starshop2.ordercontext.command.domain.ShoppingCartManagingService;
import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCart;
import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCartItem;
import com.stardata.starshop2.ordercontext.command.north.local.ShoppingCartAppService;
import com.stardata.starshop2.ordercontext.command.pl.ShoppingCartRequest;
import com.stardata.starshop2.ordercontext.command.pl.ShoppingCartResponse;
import com.stardata.starshop2.ordercontext.command.south.port.ShoppingCartRepository;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.pl.SessionUser;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/4 23:53
 */
@SpringBootTest
public class ShoppingCartTests {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 任务级测试：订单上下文——1. 保存购物车；（组合任务，领域服务）; 2. 查询购物车；（组合任务，领域服务）
     * 按照先聚合再端口、先原子再组合、从内向外的原则。
     * 设计相关任务级测试案例包括：
     * 1.1. 购物车商品ID列表和数量均正常（商品已上架且未售罄），购物车保存成功，并查询购物车信息正确；
     * 1.2. 购物车商品ID列表中有已售罄的商品ID，购物车保存正常，但区分出可下单、不可下单商品；
     * 1.3. 购物车商品ID列表中有不存在的商品ID，购物车保存异常；
     */

    @Resource
    EntityManager entityManager;

    @Autowired
    ShoppingCartManagingService shoppingCartManagingService;

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    //1.1. 购物车商品ID列表和数量均正常，购物车保存成功；
    @Test
    @Transactional
    @Rollback(true)
    void should_save_shopping_cart_successful_by_normal_product_ids_and_count() {
        //given: 给出正常已上架、且未售罄的商品ID列表和数量
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity userId = LongIdentity.from(1L);
        ShoppingCart shoppingCartToSave = ShoppingCart.of(shopId, userId);
        shoppingCartToSave.addItem(LongIdentity.from(1L), LongIdentity.from(1L), 1);
        shoppingCartToSave.addItem(LongIdentity.from(1L), LongIdentity.from(2L), 2);
        shoppingCartToSave.addItem(LongIdentity.from(1L), LongIdentity.from(3L), 3);

        //when:  调用 shoppingCartManagingService.replaceShoppingCart方法保存购物车，然后再调用queryShoppingCart查询购物车
        ShoppingCart shoppingCartSaved = shoppingCartManagingService.replaceShoppingCart(shoppingCartToSave);
        entityManager.flush();
        ShoppingCart loadedShoppingCart = shoppingCartManagingService.queryShoppingCart(userId, shopId);

        //then: 购物车保存成功，且内容正确

        assertNotNull(loadedShoppingCart);
        assertTrue(loadedShoppingCart.getTotalAmountFen()>=0L);

        List<ShoppingCartItem> cartItems = loadedShoppingCart.getItems();
        assertEquals(cartItems.size(), 3);
        ShoppingCartItem cartItem = cartItems.get(0);
        assertEquals(1L, cartItem.getProductId().value());
        assertEquals(1, cartItem.getCount());
        assertTrue(cartItem.getSubtotal().isAvailable());
        assertTrue(cartItem.getSubtotal().getTotalQuantity().value().compareTo(BigDecimal.ZERO) >0 );
        assertTrue(cartItem.getSubtotal().getAmountFen() >= 0L);

        cartItem = cartItems.get(1);
        assertEquals(2L, cartItem.getProductId().value());
        assertEquals(2, cartItem.getCount());
        assertTrue(cartItem.getSubtotal().isAvailable());
        assertTrue(cartItem.getSubtotal().getTotalQuantity().value().compareTo(BigDecimal.ZERO) >0);
        assertTrue(cartItem.getSubtotal().getAmountFen() >= 0L);

        cartItem = cartItems.get(2);
        assertEquals(3L, cartItem.getProductId().value());
        assertEquals(3, cartItem.getCount());
        assertTrue(cartItem.getSubtotal().isAvailable());
        assertTrue(cartItem.getSubtotal().getTotalQuantity().value().compareTo(BigDecimal.ZERO) >0);
        assertTrue(cartItem.getSubtotal().getAmountFen() >= 0L);

    }

    //1.2. 购物车商品ID列表中有已售罄的商品ID，购物车保存正常，但区分出可下单、不可下单商品；
    @Test
    @Transactional
    @Rollback(true)
    void should_save_shopping_cart_successful_by_normal_and_not_available_product_ids_and_count() {
        //given: 给出正常已上架、且未售罄，以及一个已售罄的商品ID列表和数量
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity userId = LongIdentity.from(1L);
        ShoppingCart shoppingCartToSave = ShoppingCart.of(shopId, userId);
        shoppingCartToSave.addItem(LongIdentity.from(1L), LongIdentity.from(1L), 1);
        shoppingCartToSave.addItem(LongIdentity.from(1L), LongIdentity.from(2L), 2);
        shoppingCartToSave.addItem(LongIdentity.from(1L), LongIdentity.from(3L), 3);
        shoppingCartToSave.addItem(LongIdentity.from(1L), LongIdentity.from(3L), 2);

        //when:  调用 shoppingCartManagingService.replaceShoppingCart方法保存购物车，然后再调用queryShoppingCart查询购物车
        ShoppingCart shoppingCartSaved = shoppingCartManagingService.replaceShoppingCart(shoppingCartToSave);
        entityManager.flush();
        ShoppingCart loadedShoppingCart = shoppingCartManagingService.queryShoppingCart(userId, shopId);

        //then: 购物车保存成功，且内容正确

        assertNotNull(loadedShoppingCart);
        assertTrue(loadedShoppingCart.getTotalAmountFen()>=0L);

        List<ShoppingCartItem> cartItems = loadedShoppingCart.getItems();
        assertEquals(cartItems.size(), 4);

        ShoppingCartItem cartItem = cartItems.get(0);
        assertEquals(1L, cartItem.getProductId().value());
        assertEquals(1, cartItem.getCount());
        assertTrue(cartItem.getSubtotal().isAvailable());
        assertTrue(cartItem.getSubtotal().getTotalQuantity().value().compareTo(BigDecimal.ZERO) >0 );
        assertTrue(cartItem.getSubtotal().getAmountFen() >= 0L);

        cartItem = cartItems.get(1);
        assertEquals(2L, cartItem.getProductId().value());
        assertEquals(2, cartItem.getCount());
        assertTrue(cartItem.getSubtotal().isAvailable());
        assertTrue(cartItem.getSubtotal().getTotalQuantity().value().compareTo(BigDecimal.ZERO) >0);
        assertTrue(cartItem.getSubtotal().getAmountFen() >= 0L);

        cartItem = cartItems.get(2);
        assertEquals(3L, cartItem.getProductId().value());
        assertEquals(3, cartItem.getCount());
        assertTrue(cartItem.getSubtotal().isAvailable());
        assertTrue(cartItem.getSubtotal().getTotalQuantity().value().compareTo(BigDecimal.ZERO) >0);
        assertTrue(cartItem.getSubtotal().getAmountFen() >= 0L);

        cartItem = cartItems.get(3);
        assertEquals(3L, cartItem.getProductId().value());
        assertEquals(2, cartItem.getCount());
        assertFalse(cartItem.getSubtotal().isAvailable());
        assertTrue(cartItem.getSubtotal().getTotalQuantity().value().compareTo(BigDecimal.ZERO) == 0);
        assertTrue(cartItem.getSubtotal().getAmountFen() == 0L);
    }

    //1.3. 购物车商品ID列表中有不存在的商品ID，购物车保存异常；
    @Test
    @Transactional
    @Rollback(true)
    void should_save_shopping_cart_exception_by_not_product_ids_and_count() {
        //given: 给出正常已上架、且未售罄的商品ID列表和数量
        LongIdentity shopId = LongIdentity.from(1L);
        LongIdentity userId = LongIdentity.from(1L);
        ShoppingCart shoppingCartToSave = ShoppingCart.of(shopId, userId);
        shoppingCartToSave.addItem(LongIdentity.from(1L), LongIdentity.from(1L), 1);
        shoppingCartToSave.addItem(LongIdentity.from(1L), LongIdentity.from(2L), 2);
        shoppingCartToSave.addItem(LongIdentity.from(1L), LongIdentity.from(3L), 3);
        shoppingCartToSave.addItem(LongIdentity.from(1L), LongIdentity.from(999L), 2);

        try {
            //when:  调用 shoppingCartManagingService.replaceShoppingCart方法保存购物车，然后再调用queryShoppingCart查询购物车
            ShoppingCart shoppingCartSaved = shoppingCartManagingService.replaceShoppingCart(shoppingCartToSave);
            entityManager.flush();
            ShoppingCart loadedShoppingCart = shoppingCartManagingService.queryShoppingCart(userId, shopId);

            //then: 下面的代码不应该被执行到，所以刻意检查一个不可能的等式
           assertEquals(1, 0);
        } catch (PersistenceException e) {
            //then: 抛出异常
            assertNotNull(e);
        }
    }

    @Autowired
    ShoppingCartAppService cartAppService;

    /**
     * 服务级测试：订单上下文——1. 保存购物车；（组合任务，应用服务）; 2. 查询购物车；（组合任务，应用服务）
     * 按照先聚合再端口、先原子再组合、从内向外的原则。
     * 设计相关任务级测试案例包括：
     * 2.1. 购物车商品ID列表和数量均正常（商品已上架且未售罄），购物车保存成功，并查询购物车信息正确；
     * 2.2. 购物车商品ID列表中有已售罄的商品ID，购物车保存正常，但区分出可下单、不可下单商品；
     */

    //2.1. 购物车商品ID列表和数量均正常，购物车保存成功；
    @Test
    @Transactional
    @Rollback(true)
    void should_save_shopping_cart_successful_by_normal_shopping_cart_request() {
        //given: 全部给出正常已上架未售罄的商品ID列表和数量作为购物车内容
        SessionUser loginUser = SessionUser.from(1L);
        Long shopId = 1L;
        ShoppingCartRequest request = new ShoppingCartRequest();
        request.item(1, 1, 1)
                .item(1, 2, 2)
                .item(1, 3, 3);

        //when:  调用 shoppingCartManagingService.replaceShoppingCart方法保存购物车，然后再调用queryShoppingCart查询购物车
        ShoppingCartResponse savedResponse = cartAppService.save(loginUser, shopId, request);
        entityManager.flush();
        ShoppingCartResponse loadedResponse = cartAppService.query(loginUser.getId(), shopId);

        //then: 购物车保存成功，且内容正确
        assertNotNull(savedResponse);
        assertTrue(savedResponse.getTotalAmountFen()>0L);

        List<ShoppingCartResponse.Item> cartItems = loadedResponse.getItems();
        assertEquals(cartItems.size(), 3);
        ShoppingCartResponse.Item cartItem = cartItems.get(0);
        assertEquals(1L, cartItem.productId());
        assertEquals(1, cartItem.count());
        assertTrue(cartItem.available());
        assertEquals(1, cartItem.orderCount());
        assertTrue(cartItem.totalQuantity().compareTo(BigDecimal.ZERO) >0 );
        assertTrue(cartItem.amountFen() >= 0L);

        cartItem = cartItems.get(1);
        assertEquals(2L, cartItem.productId());
        assertEquals(2, cartItem.count());
        assertTrue(cartItem.available());
        assertEquals(1, cartItem.orderCount());
        assertTrue(cartItem.totalQuantity().compareTo(BigDecimal.ZERO) >0);
        assertTrue(cartItem.amountFen() >= 0L);

        cartItem = cartItems.get(2);
        assertEquals(3L, cartItem.productId());
        assertEquals(3, cartItem.count());
        assertTrue(cartItem.available());
        assertEquals(3, cartItem.orderCount());
        assertTrue(cartItem.totalQuantity().compareTo(BigDecimal.ZERO) >0);
        assertTrue(cartItem.amountFen() >= 0L);
    }


    //2.2. 购物车商品ID列表中有已售罄的商品ID，购物车保存正常，但区分出可下单、不可下单商品；
    @Test
    @Transactional
    @Rollback(true)
    void should_save_shopping_cart_successful_by_normal_and_not_available_shopping_cart_request() {
        //given: 给出正常已上架且未售罄的、加已售罄的商品ID列表和数量作为购物车内容
        SessionUser loginUser = SessionUser.from(1L);
        Long shopId = 1L;
        ShoppingCartRequest request = new ShoppingCartRequest();
        request.item(1, 1, 1)
                .item(1, 2, 2)
                .item(1, 3, 3)
                .item(1, 4, 2);

        //when:  调用 shoppingCartManagingService.replaceShoppingCart方法保存购物车，然后再调用queryShoppingCart查询购物车
        ShoppingCartResponse savedResponse = cartAppService.save(loginUser, shopId, request);
        entityManager.flush();
        ShoppingCartResponse loadedResponse = cartAppService.query(loginUser.getId(), shopId);

        //then: 购物车保存成功，且内容正确，区分出来了不可用商品
        assertNotNull(savedResponse);
        assertTrue(savedResponse.getTotalAmountFen()>=0L);

        List<ShoppingCartResponse.Item> cartItems = loadedResponse.getItems();
        assertEquals(cartItems.size(), 4);
        ShoppingCartResponse.Item cartItem = cartItems.get(0);
        assertEquals(1L, cartItem.productId());
        assertEquals(1, cartItem.count());
        assertTrue(cartItem.available());
        assertEquals(1, cartItem.orderCount());
        assertTrue(cartItem.totalQuantity().compareTo(BigDecimal.ZERO) >0 );
        assertTrue(cartItem.amountFen() >= 0L);

        cartItem = cartItems.get(1);
        assertEquals(2L, cartItem.productId());
        assertEquals(2, cartItem.count());
        assertTrue(cartItem.available());
        assertEquals(1, cartItem.orderCount());
        assertTrue(cartItem.totalQuantity().compareTo(BigDecimal.ZERO) >0);
        assertTrue(cartItem.amountFen() >= 0L);

        cartItem = cartItems.get(2);
        assertEquals(3L, cartItem.productId());
        assertEquals(3, cartItem.count());
        assertTrue(cartItem.available());
        assertEquals(3, cartItem.orderCount());
        assertTrue(cartItem.totalQuantity().compareTo(BigDecimal.ZERO) >0);
        assertTrue(cartItem.amountFen() >= 0L);

        cartItem = cartItems.get(3);
        assertEquals(4L, cartItem.productId());
        assertEquals(2, cartItem.count());
        assertFalse(cartItem.available());
        assertEquals(0, cartItem.orderCount());
        assertEquals(0, cartItem.totalQuantity().compareTo(BigDecimal.ZERO));
        assertEquals(0L, (long) cartItem.amountFen());
    }
}
