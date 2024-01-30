package com.stardata.starshop2.simulator;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2024/1/30 09:07
 */
@Getter
public class ExceptionRateConfig {
    private ExceptionRateConfig(){}
    public static final Map<String, Double> rateValues = new HashMap<>();

    public static final String TIMEOUT_SECONDS = "timeout_seconds";
    public static final String SETTLE_PRODUCTS_TIMEOUT = "settle_products_timeout";
    public static final String CUSTOMER_ID_EXCEPTION = "customer_id_exception";
    public static final String ORDER_SAVE_TIMEOUT = "order_save_timeout";
    public static final String ORDER_SAVE_EXCEPTION = "order_save_exception";
}
