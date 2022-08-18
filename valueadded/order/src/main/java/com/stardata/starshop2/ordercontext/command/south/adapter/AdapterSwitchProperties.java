package com.stardata.starshop2.ordercontext.command.south.adapter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/8/13 22:37
 */
@Data
//@Component
@ConfigurationProperties(prefix = "adapter.client")
public class AdapterSwitchProperties {
    /**
     * 设置订单项结算客户端
     */
    private String orderItemsSettlement;
}
