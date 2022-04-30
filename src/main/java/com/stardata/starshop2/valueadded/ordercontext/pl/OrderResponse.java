package com.stardata.starshop2.valueadded.ordercontext.pl;

import com.stardata.starshop2.valueadded.ordercontext.command.domain.order.Order;
import lombok.Data;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/29 13:57
 */
@Data
public class OrderResponse {
    public static OrderResponse from(Order order) {
        //todo 完成将订单对象转DTO
        return null;
    }
}
