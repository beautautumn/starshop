package com.stardata.starshop2.ordercontext.command.south.adapter;

import com.stardata.starshop2.ordercontext.command.south.port.OrderEventPublisher;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import com.stardata.starshop2.sharedcontext.domain.DomainEvent;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/6 23:12
 */
@Adapter(PortType.Publisher)
@Component
@AllArgsConstructor
public class OrderEventPublisherAdapter implements OrderEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(@NotNull DomainEvent event) {
        String message = event.toString();
        kafkaTemplate.send(event.getTopic(), message);
    }
}
