package com.stardata.starshop2.ordercontext.command.south.adapter;

import com.stardata.starshop2.ordercontext.command.south.port.OrderEventPublisher;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import com.stardata.starshop2.sharedcontext.domain.DomainEvent;
import org.springframework.stereotype.Component;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/6 23:12
 */
@Adapter(PortType.Publisher)
@Component
public class OrderEventPublisherAdapter implements OrderEventPublisher {
    @Override
    public void publish(DomainEvent event) {

    }
}
