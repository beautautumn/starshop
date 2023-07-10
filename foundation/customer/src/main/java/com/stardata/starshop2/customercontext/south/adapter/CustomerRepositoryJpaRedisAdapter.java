package com.stardata.starshop2.customercontext.south.adapter;

import com.stardata.starshop2.customercontext.domain.customer.Customer;
import com.stardata.starshop2.customercontext.south.port.CustomerRepository;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.south.adapter.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/7 20:04
 */
@Adapter(PortType.Repository)
@Component("customerRepositoryJpaRedisAdapter")
public class CustomerRepositoryJpaRedisAdapter implements CustomerRepository {
    private final GenericRepository<Customer, LongIdentity> repository;

    @Autowired
    private RedisTemplate<String, Object> redisCacheTemplate;

    public CustomerRepositoryJpaRedisAdapter(GenericRepository<Customer, LongIdentity> repository) {
        this.repository = repository;
    }

    @Override
    public void add(Customer customer) {
        repository.saveOrUpdate(customer);
    }

    @Override
    public Customer instanceOf(LongIdentity customerId) {
        return repository.findById(customerId);
    }

    @Override
    public Customer findByUserId(LongIdentity userId) {
        String customerKey = "customerCache-byUserId-"+userId.toString();
        Customer customer = (Customer)redisCacheTemplate.opsForValue().get(customerKey);
        if (customer == null) {
            Specification<Customer> specification = (root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("userId"), userId.value());

            List<Customer> customers = repository.findBy(specification);
            customer =  customers.size()>0?customers.get(0): null;

            redisCacheTemplate.opsForValue().set(customerKey, customer);
        }
        return customer;
    }

    @Override
    public void update(Customer customer) {
        repository.saveOrUpdate(customer);
    }
}
