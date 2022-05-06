package com.stardata.starshop2.valueadded.ordercontext.command.domain.order;

import lombok.Data;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:28
 */
@Data
public class WxPayResult {
  private String outTradeNo;

  WxPayResult(){}
}
