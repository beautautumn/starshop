package com.stardata.starshop2.ordercontext.command.pl;

import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import lombok.Data;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/29 13:58
 */
@Data
public class OrderSubmitRequest {
    public Order toOrder(Long userId, Long shopId) {
        //todo 完成根据DTO创建订单的工厂方法
        return null;
    }
}
