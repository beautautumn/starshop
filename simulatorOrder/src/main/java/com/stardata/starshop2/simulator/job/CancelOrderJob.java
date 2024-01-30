package com.stardata.starshop2.simulator.job;

import com.stardata.starshop2.ordercontext.command.north.local.OrderAppService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @author Samson Shu
 * @email shush@stardata.top
 * @date 2020/7/15 15:34
 */
@Component
@Getter
@Setter
@Slf4j
public class CancelOrderJob extends QuartzJobBean {
  @Autowired
  private OrderAppService appService;
  private String key;

  @Override
  protected void executeInternal(@NotNull JobExecutionContext jobExecutionContext) {

    log.trace("<== CancelOrPayOrderJob executeInternal..."+System.currentTimeMillis());
    appService.autoCancel();
  }
}
