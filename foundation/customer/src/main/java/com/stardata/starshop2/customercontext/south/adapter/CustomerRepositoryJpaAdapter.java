package com.stardata.starshop2.customercontext.south.adapter;

import com.stardata.starshop2.customercontext.domain.customer.Customer;
import com.stardata.starshop2.customercontext.south.port.CustomerRepository;
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
 * @date 2023/7/7 20:04
 */
@Adapter(PortType.Repository)
@Component
public class CustomerRepositoryJpaAdapter implements CustomerRepository {
    private final GenericRepository<Customer, LongIdentity> repository;

    public CustomerRepositoryJpaAdapter(GenericRepository<Customer, LongIdentity> repository) {
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
        Specification<Customer> specification = (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userId"), userId.value());

        List<Customer> customers = repository.findBy(specification);
        return customers.size()>0?customers.get(0): null;
    }

    @Override
    public void update(Customer customer) {
        repository.saveOrUpdate(customer);
    }
}
