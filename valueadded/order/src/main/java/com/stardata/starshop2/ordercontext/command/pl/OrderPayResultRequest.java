package com.stardata.starshop2.ordercontext.command.pl;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.stardata.starshop2.ordercontext.command.domain.order.PayResult;
import com.thoughtworks.xstream.XStream;
import lombok.Data;
import me.chanjar.weixin.common.util.xml.XStreamInitializer;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/30 16:55
 */
@Data
public class OrderPayResultRequest {
    private final String outTradeNo;
    private final String transactionId;
    private final String payTimeStr;
    private final int cashFeeFen;
    private final String resultMessage;

    public OrderPayResultRequest(WxPayOrderNotifyResult notifyResult) {

        this.outTradeNo = notifyResult.getOutTradeNo();
        this.transactionId = notifyResult.getTransactionId();
        this.payTimeStr = notifyResult.getTimeEnd();
        this.cashFeeFen = notifyResult.getCashFee()==null?0:notifyResult.getCashFee();

        XStream xstream = XStreamInitializer.getInstance();
        xstream.processAnnotations(notifyResult.getClass());
        this.resultMessage = xstream.toXML(notifyResult);

    }

    public PayResult toWxPayResult() {
        //todo 完成根据微信支付DTO创建微信支付值对象的工厂方法
        return null;
    }
}
