package com.stardata.starshop2.ordercontext.command.pl;

import com.stardata.starshop2.ordercontext.command.domain.order.PrepayOrder;
import lombok.Data;

import java.util.Map;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/29 16:08
 */
@Data
public class PrepayOrderResponse {
    private String prepayId;
    private Map<String,String> appResult;

    public static PrepayOrderResponse from(PrepayOrder prepay) {
        return PrepayOrderResponseMapper.INSTANCE.convert(prepay);
    }
}
