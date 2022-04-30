package com.stardata.starshop2.valueadded.ordercontext.pl;

import com.stardata.starshop2.valueadded.ordercontext.command.domain.order.Order;
import lombok.Data;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/30 20:34
 */
@Data
public class OrderConfirmedResponse {
    public static OrderConfirmedResponse from(Order order) {
        //todo 完成订单转订单确认后的返回DTO转换
        return null;
    }
}
