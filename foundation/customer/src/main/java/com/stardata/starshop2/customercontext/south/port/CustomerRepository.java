package com.stardata.starshop2.customercontext.south.port;

import com.stardata.starshop2.customercontext.domain.customer.Customer;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/7 20:02
 */
public interface CustomerRepository {
    void add(Customer customer);

    Customer instanceOf(LongIdentity customerId);

    Customer findByUserId(LongIdentity userId);

    void update(Customer customer);
}
