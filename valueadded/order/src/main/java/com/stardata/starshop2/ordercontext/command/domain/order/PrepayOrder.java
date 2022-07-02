package com.stardata.starshop2.ordercontext.command.domain.order;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepayOrder {
    // 所有支付平台都需要的工作结果字段
    private LongIdentity orderId;
    private String appId;
    private String prepayId;

    //具体平台要求的结果，保存到一个JSON串中
    private String appJsonResult;
}
