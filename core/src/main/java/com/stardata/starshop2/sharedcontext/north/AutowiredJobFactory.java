package com.stardata.starshop2.sharedcontext.north;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * @author Samson Shu
 * @email shush@stardata.top
 * @date 2020/7/15 20:55
 */
@Component
@AllArgsConstructor
public class AutowiredJobFactory extends AdaptableJobFactory {

  private final AutowireCapableBeanFactory capableBeanFactory;

  @NotNull
  @Override
  protected Object createJobInstance(@NotNull TriggerFiredBundle bundle) throws Exception {
    Object jobInstance = super.createJobInstance(bundle);
    capableBeanFactory.autowireBean(jobInstance);
    return jobInstance;
  }
}
