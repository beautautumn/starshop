package com.stardata.starshop2.ordercontext.command.domain;

import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import com.stardata.starshop2.sharedcontext.domain.SessionUser;
import org.jetbrains.annotations.NotNull;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/7/2 17:45
 */
public interface PrepayRequestGenerationService {
    String ORDER_URL_BASE = "https://starshop_apis.stardata.top/v2";
    String WX_NOTIFY_ORDER_PAY_URL = ORDER_URL_BASE + "/orders/wxnotify/pay";
    String APP_NAME = "群买菜";

    class TradeName {
        public static final String BUY_PRODUCT = "购买商品";
        public static final String CHAIN_ORDER = "接龙下单";
        public static final String BUY_SHOPSET = "购买服务套餐";
    }

    String generatePrepay(@NotNull SessionUser user, @NotNull Order order);
}
