package com.stardata.starshop2.ordercontext.command.south.adapter;

import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import com.stardata.starshop2.ordercontext.command.south.port.OrderRepository;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.util.Repository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/9 21:41
 */
public class OrderRepositoryJpaAdapter  implements OrderRepository {
    private final Repository<Order, LongIdentity> repository;

    public OrderRepositoryJpaAdapter(Repository<Order, LongIdentity> repository, EntityManager entityManager) {
        this.repository = repository;
    }

    @Override
    public void add(Order order) {
        repository.saveOrUpdate(order);
    }

    @Override
    public Order instanceOf(LongIdentity orderId) {
        return repository.findById(orderId);
    }

    @Override
    public Order findByOutTradeNo(String outTradeNo) {
        Specification<Order> specification = (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("order_number"), outTradeNo);

        List<Order> orders = repository.findBy(specification);
        return orders.size()>0?orders.get(0): null;
    }

    @Override
    public void update(Order order) {
        repository.saveOrUpdate(order);
    }

    @Override
    public List<Order> findConfirmExpired(int maxMinutes) {
        return null;
    }

    @Override
    public List<Order> findPayExpired(int maxMinutes) {
        return null;
    }
}
