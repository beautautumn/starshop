package com.stardata.starshop2.simulator;

import com.stardata.starshop2.sharedcontext.pl.EventConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/9/10 21:02
 */
@Configuration
public class TopicConfiguration {
    @Bean
    public NewTopic newTopic() {
        return new NewTopic(EventConstants.EventTopic.ORDER_EVENT, 1, (short) 1);
    }
}
