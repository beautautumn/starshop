package com.stardata.starshop2.ordercontext.command.pl;

import com.stardata.starshop2.ordercontext.command.domain.order.WxPrepayOrder;
import lombok.Data;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/29 16:08
 */
@Data
public class PrepayResponse {
    public static PrepayResponse from(WxPrepayOrder prepay) {
        //todo WxPrepayOrder转DTO
        return null;
    }
}
