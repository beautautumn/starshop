package com.stardata.starshop2.ordercontext.command.pl;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.stardata.starshop2.ordercontext.command.domain.order.PayResult;
import com.thoughtworks.xstream.XStream;
import lombok.Data;
import me.chanjar.weixin.common.util.xml.XStreamInitializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private final LocalDateTime payTime;
    private final long cashFeeFen;
    private final String resultMessage;
    private final boolean success;

    public OrderPayResultRequest(WxPayOrderNotifyResult notifyResult) {

        this.outTradeNo = notifyResult.getOutTradeNo();
        this.transactionId = notifyResult.getTransactionId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        this.payTime = LocalDateTime.parse(notifyResult.getTimeEnd(), formatter);
        this.cashFeeFen = notifyResult.getCashFee()==null?0:notifyResult.getCashFee();
        this.success = notifyResult.getResultCode().equals("SUCCESS");

        XStream xstream = XStreamInitializer.getInstance();
        xstream.processAnnotations(notifyResult.getClass());
        this.resultMessage = xstream.toXML(notifyResult);

    }

    public PayResult toPayResult() {
        return OrderPayResultRequestMapper.INSTANCE.convert(this);
    }
}
