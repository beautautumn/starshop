package com.stardata.starshop2.ordercontext.command.south.adapter;

import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import com.stardata.starshop2.ordercontext.command.south.port.OrderRepository;
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
 * @date 2022/5/9 21:41
 */
@Adapter(PortType.Repository)
@Component
public class OrderRepositoryJpaAdapter  implements OrderRepository {
    private final GenericRepository<Order, LongIdentity> repository;

    public OrderRepositoryJpaAdapter(GenericRepository<Order, LongIdentity> repository) {
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
                criteriaBuilder.equal(root.get("orderNumber"), outTradeNo);

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
