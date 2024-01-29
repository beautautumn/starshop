package com.stardata.starshop2.ordercontext.command.north.job;

import com.stardata.starshop2.sharedcontext.north.AutowiredJobFactory;
import lombok.AllArgsConstructor;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import java.util.Objects;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2024/1/29 11:25
 */
@Configuration
@AllArgsConstructor
public class QuartzConfig {
    private final AutowiredJobFactory jobFactory;

    //用于后台定时自动超时处理订单支付、订单关闭的队列名称
    public static final String TOPAY_QUEUE_NAME = "OrderToPay";
    public static final String TOCLOSE_QUEUE_NAME = "OrderToClose";

    @Bean
    JobDetailFactoryBean cancelOrderJobDetail() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setJobClass(CancelOrderJob.class);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("key", TOPAY_QUEUE_NAME);
        bean.setJobDataMap(jobDataMap);
        bean.setDurability(true);
        return bean;
    }

    @Bean
    JobDetailFactoryBean closeOrderJobDetail() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setJobClass(ConfirmOrderJob.class);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("key", TOCLOSE_QUEUE_NAME);
        bean.setJobDataMap(jobDataMap);
        bean.setDurability(true);
        return bean;
    }

    @Bean
    CronTriggerFactoryBean cronTrigger4CancelOrder() {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(Objects.requireNonNull(cancelOrderJobDetail().getObject()));
        bean.setCronExpression("0 * * * * ?");
        return bean;
    }

    @Bean
    CronTriggerFactoryBean cronTrigger4CloseOrder() {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(Objects.requireNonNull(closeOrderJobDetail().getObject()));
        bean.setCronExpression("30 * * * * ?");
        return bean;
    }

    @Bean
    SchedulerFactoryBean schedulerFactory() {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        CronTrigger cronTrigger1 = cronTrigger4CloseOrder().getObject();
        CronTrigger cronTrigger2 = cronTrigger4CancelOrder().getObject();
        factory.setTriggers(cronTrigger1, cronTrigger2);

        factory.setOverwriteExistingJobs(true);
        factory.setStartupDelay(20);
        factory.setJobFactory(jobFactory);

        return factory;
    }


}
