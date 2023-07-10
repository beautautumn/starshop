package com.stardata.starshop2.customercontext.north.local;

import com.stardata.starshop2.customercontext.domain.customer.Customer;
import com.stardata.starshop2.customercontext.pl.CustomerToDtoMapper;
import com.stardata.starshop2.customercontext.south.port.CustomerRepository;
import com.stardata.starshop2.pl.CustomerInfoDto;
import com.stardata.starshop2.pl.UserInfoDto;
import com.stardata.starshop2.sharedcontext.domain.Gender;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.domain.MobileNumber;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/7 19:46
 */
@Service
@AllArgsConstructor
public class CustomerAppService {
    @Resource(name="${adapter.customerReposity}")
    private CustomerRepository customerRepository;

    @Transactional
    public CustomerInfoDto ensureCustomerByUser(@NotNull UserInfoDto userInfoDTO, @NotNull Long shopId) {
        Customer customer = customerRepository.findByUserId(LongIdentity.from(userInfoDTO.getId()));
        if (customer == null) {
            customer = Customer.of(
                    LongIdentity.from(shopId),
                    LongIdentity.from(userInfoDTO.getId()),
                    userInfoDTO.getNickName(),
                    MobileNumber.from(userInfoDTO.getMobileNumber()),
                    Gender.of(userInfoDTO.getGender())
            );
            customer
                    .avatarUrl(userInfoDTO.getAvatarUrl())
                    .country(userInfoDTO.getCountry())
                    .province(userInfoDTO.getProvince())
                    .city(userInfoDTO.getCity());
            customerRepository.add(customer);
        }
        return CustomerToDtoMapper.INSTANCE.convert(customer);
    }

    public CustomerInfoDto getCustomerByUserId(@NotNull Long userId) {
        Customer customer = customerRepository.findByUserId(LongIdentity.from(userId));
        return CustomerToDtoMapper.INSTANCE.convert(customer);
    }
}
