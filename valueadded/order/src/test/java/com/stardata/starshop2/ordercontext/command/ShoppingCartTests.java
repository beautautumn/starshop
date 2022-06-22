package com.stardata.starshop2.ordercontext.command;

import com.stardata.starshop2.ordercontext.command.domain.ShoppingCartManagingService;
import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCart;
import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCartItem;
import com.stardata.starshop2.ordercontext.command.south.port.ShoppingCartRepository;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

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
     * 任务级测试：微信用户登录——1. 保存购物车；（组合任务，领域服务）
     * 按照先聚合再端口、先原子再组合、从内向外的原则。
     * 设计相关任务级测试案例包括：
     * 1.1. 购物车商品ID列表和数量均正常（商品已上架且未售罄），购物车保存成功；
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
        shoppingCartToSave.addItem(LongIdentity.from(1L), LongIdentity.from(2L), 1);
        shoppingCartToSave.addItem(LongIdentity.from(1L), LongIdentity.from(3L), 1);

        //when:  调用 shoppingCartManagingService.replaceShoppingCart方法
        ShoppingCart shoppingCartSaved = shoppingCartManagingService.replaceShoppingCart(shoppingCartToSave);
        entityManager.flush();

        //then: 购物车保存成功，且内容正确
        ShoppingCart loadedShoppingCart = shoppingCartRepository.findForUserInShop(userId, shopId);

        assertNotNull(loadedShoppingCart);
        assertTrue(loadedShoppingCart.getTotalAmountFen()>=0L);

        List<ShoppingCartItem> cartItems = loadedShoppingCart.getItems();
        assertEquals(cartItems.size(), 3);
        ShoppingCartItem cartItem = cartItems.get(0);
        assertEquals(1L, cartItem.getProductId().value());
        assertEquals(1, cartItem.getCount());
        assertTrue(cartItem.getSubtotal().isAvailable());
        assertTrue(cartItem.getSubtotal().getTotalQuantity() > 0L);
        assertTrue(cartItem.getSubtotal().getAmountFen() >= 0L);

        cartItem = cartItems.get(1);
        assertEquals(2L, cartItem.getProductId().value());
        assertEquals(1, cartItem.getCount());
        assertTrue(cartItem.getSubtotal().isAvailable());
        assertTrue(cartItem.getSubtotal().getTotalQuantity() > 0L);
        assertTrue(cartItem.getSubtotal().getAmountFen() >= 0L);

        cartItem = cartItems.get(2);
        assertEquals(3L, cartItem.getProductId().value());
        assertEquals(1, cartItem.getCount());
        assertTrue(cartItem.getSubtotal().isAvailable());
        assertTrue(cartItem.getSubtotal().getTotalQuantity() > 0L);
        assertTrue(cartItem.getSubtotal().getAmountFen() >= 0L);

    }

}
