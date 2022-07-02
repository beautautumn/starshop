package com.stardata.starshop2.ordercontext.command.domain;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.stardata.starshop2.ordercontext.command.domain.order.Order;
import com.stardata.starshop2.ordercontext.command.domain.order.OrderType;
import com.stardata.starshop2.sharedcontext.domain.BizParameter;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.domain.SessionUser;
import com.stardata.starshop2.sharedcontext.helper.JSONUtil;
import com.stardata.starshop2.sharedcontext.south.port.BizParameterRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/7/2 17:48
 */
@AllArgsConstructor
@Component
public class WxPrepayRequestGenerationService implements PrepayRequestGenerationService{
    private final BizParameterRepository parameterRepository;

    @Override
    public String generatePrepay(@NotNull SessionUser user, @NotNull Order order) {
        WxPayUnifiedOrderRequest prepayRequest = new WxPayUnifiedOrderRequest();
        prepayRequest.setOutTradeNo(order.getOrderNumber());
        prepayRequest.setFeeType("CNY");
        prepayRequest.setTotalFee(order.getTotalAmountFen().intValue());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeStart = order.getCreateTime().format(formatter);
        BizParameter parameter = parameterRepository.instanceOf("max_minutes_remain_topay");
        //如果数据库未配置参数，则默认30分钟支付订单（额外加1分钟，以支持系统延迟容错）
        int maxMinutes = parameter.toInteger(30);
        String timeExpire = order.getCreateTime().
                plusMinutes(maxMinutes + 1).format(formatter);
        prepayRequest.setTimeStart(timeStart);
        prepayRequest.setTimeExpire(timeExpire);
        prepayRequest.setSignType("MD5");
        prepayRequest.setTradeType("JSAPI");
        prepayRequest.setSceneInfo("");

        prepayRequest.setBody(String.format("%s-%s", APP_NAME, order.getType().equals(OrderType.SHOP)
                ?TradeName.BUY_PRODUCT:TradeName.CHAIN_ORDER));
        prepayRequest.setAttach(APP_NAME);
        prepayRequest.setNotifyUrl(WX_NOTIFY_ORDER_PAY_URL);

        ObjectNode detailObject = toWxPayDetailObject(order);
        detailObject.put("receipt_id", order.getOrderNumber());
        prepayRequest.setDetail(detailObject.toString());

        prepayRequest.setDeviceInfo(generateDeviceInfo(order.getShopId(), user.getId()));
        prepayRequest.setOpenid(user.getOpenid());
        return prepayRequest.toXML();
    }

    private  String generateDeviceInfo(LongIdentity shopId, LongIdentity userId) {
        String shopIdStr = shopId.toString();
        String userIdStr = userId.toString();
        if (shopIdStr.length()>10) shopIdStr = shopIdStr.substring(0, 10);
        if (userIdStr.length()>22) userIdStr = userIdStr.substring(0, 22);
        long id1 = Long.parseLong(shopIdStr);
        long id2 = Long.parseLong(userIdStr);
        return String.format("%010d%022d", id1, id2);
    }

    private ObjectNode toWxPayDetailObject(Order order) {
        ObjectNode result = JSONUtil.createObjectNode();
        result.put("cost_price", order.getTotalAmountFen());
        List<Map<String, Object>> goods_detail = new ArrayList<>();
        order.getItems().forEach(item -> {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("goods_id", item.getProductId().value());
            itemMap.put("goods_name", item.getProductName());
            itemMap.put("quantity", item.getPurchaseCount());
            itemMap.put("price", item.getSubtotalFen());

            goods_detail.add(itemMap);
        });
        result.put("goods_detail", JSONUtil.toJSONString(goods_detail));
        return result;

    }

}
