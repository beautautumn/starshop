package com.stardata.starshop2.ordercontext.command.south.adapter;

import cn.hutool.core.net.NetUtil;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.util.SignUtils;
import com.stardata.starshop2.ordercontext.command.domain.order.OrderPayment;
import com.stardata.starshop2.ordercontext.command.domain.order.PrepayOrder;
import com.stardata.starshop2.ordercontext.command.south.port.PrepayingClient;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import com.stardata.starshop2.sharedcontext.exception.ApplicationInfrastructureException;
import com.stardata.starshop2.sharedcontext.exception.ApplicationValidationException;
import com.stardata.starshop2.sharedcontext.helper.CharUtil;
import com.thoughtworks.xstream.XStream;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.util.xml.XStreamInitializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/6 11:21
 */
@Adapter(PortType.Client)
@Component
@AllArgsConstructor
public class PrepayingClientWePayAdapter implements PrepayingClient {
    private final WxPayService wxService;

    @Override
    public PrepayOrder prepay(OrderPayment payment) {

        //1. 先解析原来创建订单支付时，已经创建好的支付平台请求字符串
        WxPayUnifiedOrderRequest prepayRequest;
        try {
            XStream xstream = XStreamInitializer.getInstance();
            xstream.processAnnotations(WxPayUnifiedOrderRequest.class);
            prepayRequest =
                    (WxPayUnifiedOrderRequest)xstream.fromXML(payment.getRequestMessage());
        } catch (Exception e) {
            throw new ApplicationValidationException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    String.format("微信预支付请求字符串不是有效的XML: %s", e.getMessage()));
        }
        //...

        //2. 调用微信支付平台接口创建统一支付
        HashSet<String> localIps = NetUtil.localIpv4s();
        String localIp = "";
        if (localIps.size()>0) localIp = (String)localIps.toArray()[0];

        prepayRequest.setSpbillCreateIp(localIp);
        prepayRequest.setNonceStr(CharUtil.getRandomString(32));
        WxPayUnifiedOrderResult unifiedOrderResult;

        try {
            unifiedOrderResult = wxService.unifiedOrder(prepayRequest);
        } catch (WxPayException e) {
            throw new ApplicationInfrastructureException(HttpStatus.SERVICE_UNAVAILABLE.value(),
                    String.format("微信统一支付接口报错，错误代码： '%s'，信息：%s。", e.getErrCode(), e.getErrCodeDes()));
        }

        //3. 对微信支付平台返回的请求结果进行校验
        String prepayId = unifiedOrderResult.getPrepayId();
        if (StringUtils.isBlank(prepayId)) {
            throw new ApplicationInfrastructureException(HttpStatus.SERVICE_UNAVAILABLE.value(),
                    String.format("无法获取prepay id，错误代码： '%s'，信息：%s。", unifiedOrderResult.getErrCode(), unifiedOrderResult.getErrCodeDes()));
        }

        //4. 更新payment
        payment.setPrepayId(prepayId);

        //5. 将微信支付返回的结果打包成可使用的结构体
        String nonceStr = unifiedOrderResult.getNonceStr();
        String appId = unifiedOrderResult.getAppid();
        String signType = prepayRequest.getSignType();
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        Map<String, String> appResult = new HashMap<>();
        appResult.put("appId", appId);
        appResult.put("timeStamp", timeStamp);
        appResult.put("nonceStr", nonceStr);
        appResult.put("package", "prepay_id=" + prepayId);
        appResult.put("signType", WxPayConstants.SignType.MD5);
        String paySign = SignUtils.createSign(appResult, signType,
                wxService.getConfig().getMchKey(), null);
        appResult.put("paySign", paySign);

        return PrepayOrder.builder()
                .orderId(payment.getOrder().getId())
                .appId(appId)
                .prepayId(prepayId)
                .appResult(appResult)
                .build();
    }
}
