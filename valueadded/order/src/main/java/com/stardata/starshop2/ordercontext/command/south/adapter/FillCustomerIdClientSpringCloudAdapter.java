package com.stardata.starshop2.ordercontext.command.south.adapter;

import com.stardata.starshop2.api.CustomerBizService;
import com.stardata.starshop2.api.UserBizService;
import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import com.stardata.starshop2.ordercontext.command.south.port.FillCustomerIdClient;
import com.stardata.starshop2.pl.CustomerInfoDto;
import com.stardata.starshop2.pl.UserInfoDto;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.exception.ApplicationValidationException;
import com.stardata.starshop2.sharedcontext.helper.JSONUtil;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/7 19:35
 */
@Adapter(PortType.Client)
@Component("fillCustomerIdClientSpringCloudAdapter")
@AllArgsConstructor
public class FillCustomerIdClientSpringCloudAdapter implements FillCustomerIdClient {
    private final CustomerBizService customerBizService;
    private final UserBizService userBizService;

    @Override
    public void fillCustomerId(@NotNull Order order) {
        UserInfoDto userInfoDTO = userBizService.getUserInfo(order.getUserId().value());
        if (userInfoDTO == null) {
            throw new ApplicationValidationException("Invalid order userId: "+ order.getUserId().value());
        }

        CustomerInfoDto customerInfoDTO = customerBizService.ensureUserCustomerInfo(
                userInfoDTO, order.getShopId().value());
        if (customerInfoDTO == null) {
            throw new ApplicationValidationException("Can't make customer for userInfo: " +
                    JSONUtil.toJSONString(userInfoDTO));
        }
        order.setCustomerId(LongIdentity.from(customerInfoDTO.getId()));
    }
}
