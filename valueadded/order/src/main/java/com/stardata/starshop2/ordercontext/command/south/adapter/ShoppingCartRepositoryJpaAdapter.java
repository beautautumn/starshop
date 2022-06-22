package com.stardata.starshop2.ordercontext.command.south.adapter;

import com.stardata.starshop2.ordercontext.command.domain.shoppingcart.ShoppingCart;
import com.stardata.starshop2.ordercontext.command.south.port.ShoppingCartRepository;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.south.adapter.GenericRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/6 23:08
 */
@Adapter(PortType.Repository)
@Component
public class ShoppingCartRepositoryJpaAdapter implements ShoppingCartRepository {
    private final GenericRepository<ShoppingCart, LongIdentity> repository;

    public ShoppingCartRepositoryJpaAdapter(GenericRepository<ShoppingCart, LongIdentity> repository) {
        this.repository = repository;
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        ShoppingCart oldCart = findForUserInShop(shoppingCart.getUserId(), shoppingCart.getShopId());
        if (oldCart != null) {
            oldCart.getItems().clear();
            oldCart.getItems().addAll(shoppingCart.getItems());
            shoppingCart = oldCart;
        }
        repository.saveOrUpdate(shoppingCart);
    }

    @Override
    public ShoppingCart findForUserInShop(LongIdentity userId, LongIdentity shopId) {
        Specification<ShoppingCart> specification = (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.and( criteriaBuilder.equal(root.get("userId"), userId.value()),
                                criteriaBuilder.equal(root.get("shopId"), shopId.value()));
        List<ShoppingCart> shoppingCarts = repository.findBy(specification);
        return shoppingCarts.size()>0 ? shoppingCarts.get(0) : null;
    }
}
