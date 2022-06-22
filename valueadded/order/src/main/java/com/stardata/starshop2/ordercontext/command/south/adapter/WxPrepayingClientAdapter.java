package com.stardata.starshop2.ordercontext.command.south.adapter;

import com.stardata.starshop2.ordercontext.command.domain.order.OrderPayment;
import com.stardata.starshop2.ordercontext.command.domain.order.WxPrepayOrder;
import com.stardata.starshop2.ordercontext.command.south.port.WxPrepayingClient;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import org.springframework.stereotype.Component;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/6 11:21
 */
@Adapter(PortType.Client)
@Component
public class WxPrepayingClientAdapter implements WxPrepayingClient {
    @Override
    public WxPrepayOrder prepay(OrderPayment payment) {
        return null;
    }
}
