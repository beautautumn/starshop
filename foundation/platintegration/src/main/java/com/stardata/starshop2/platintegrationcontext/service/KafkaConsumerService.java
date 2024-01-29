package com.stardata.starshop2.platintegrationcontext.service;

import com.stardata.starshop2.sharedcontext.pl.EventConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2024/1/29 19:11
 */
@Slf4j
@Service
@AllArgsConstructor
public class KafkaConsumerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(id = "consumeOrderEvent", topics = EventConstants.EventTopic.ORDER_EVENT)
    public void consumeOrderEvent(String message) {
        log.info("consumerSingle ====> message: {}", message);
    }
}
