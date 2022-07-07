package com.stardata.starshop2.ordercontext.command.domain.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/18 15:28
 */
@Getter
@Setter
@Builder
public class PayResult {
  private String outTradeNo;
  private boolean success;
  private LocalDateTime payTime;
  private String transactionId;
  private Long cashFeeFen;
  private String resultMessage;

}
