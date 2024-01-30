package com.stardata.starshop2.simulator;

import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.persistence.QueryTimeoutException;
import java.util.Random;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2024/1/30 09:33
 */
@Component
@Aspect
@AllArgsConstructor
public class SimulatorAspect {
    private static final Random random = new Random();

    private boolean rateSatisfied(String key) {
        double rate = random.nextDouble();
        Double rateSimulator = ExceptionRateConfig.rateValues.get(key);
        if (rateSimulator == null) {
            rateSimulator = 0.0;
        }
        return (rate < rateSimulator);
    }

    private long timeoutSeconds() {
        Double value = ExceptionRateConfig.rateValues.get(ExceptionRateConfig.TIMEOUT_SECONDS);
        if (value == null) {
            return 25000L;
        } else {
            return value.longValue();
        }
    }

    //模拟产品结算100秒超时
    @Pointcut("execution(* com.stardata.starshop2.ordercontext.command.south.adapter.OrderSettlementClientSpringCloudAdapter.settleProducts(..))")
    public void pcSettleProducts() {}

    @AfterReturning("pcSettleProducts()")
    public void afterOrderSubmitRequestToOrder(JoinPoint jp) throws InterruptedException {
        if (rateSatisfied(ExceptionRateConfig.SETTLE_PRODUCTS_TIMEOUT)) {
            Thread.sleep(timeoutSeconds()*1000);
        }
    }

    //模拟获取客户ID系统异常
    @Pointcut("execution(* com.stardata.starshop2.ordercontext.command.south.adapter.FillCustomerIdClientSpringCloudAdapter.fillCustomerId(..))")
    public void pcFillCustomerId() {}

    @AfterReturning("pcFillCustomerId()")
    public void afterFillCustomerId(JoinPoint jp) throws InterruptedException {
        if (rateSatisfied(ExceptionRateConfig.CUSTOMER_ID_EXCEPTION)) {
            Long.valueOf("模拟 long 整数转换异常");
        }
    }


    //模拟订单数据库报错、超时
    @Pointcut("execution(* com.stardata.starshop2.ordercontext.command.south.adapter.OrderRepositoryJpaAdapter.*(..))")
    public void pcOrderRepository() {}

    @AfterReturning("pcOrderRepository()")
    public void afterOrderRepository(JoinPoint jp) throws InterruptedException {
        if (rateSatisfied(ExceptionRateConfig.ORDER_SAVE_TIMEOUT)) {
            Thread.sleep(timeoutSeconds()*1000);
        }
    }

    @Before("pcOrderRepository()")
    public void beforeOrderRepository(JoinPoint jp) throws InterruptedException {
        if (rateSatisfied(ExceptionRateConfig.ORDER_SAVE_EXCEPTION)) {
            throw new QueryTimeoutException("模拟数据库连接超时异常");
        }
    }
}
