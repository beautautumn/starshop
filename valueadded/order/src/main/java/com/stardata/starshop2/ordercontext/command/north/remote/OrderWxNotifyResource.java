package com.stardata.starshop2.ordercontext.command.north.remote;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.stardata.starshop2.ordercontext.command.north.local.OrderAppService;
import com.stardata.starshop2.sharedcontext.annotation.IgnoreAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/29 13:53
 */
@Api(tags = "订单命令类资源接口")
@RestController
@RequestMapping("/v2")
@AllArgsConstructor
public class OrderWxNotifyResource {

    @Autowired
    private HttpServletRequest request;

//    private final WxPayService wxService;

    private final OrderAppService appService;

    private String getStringRequest() {
        String result = null;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            char[] buff = new char[2048];
            int len;
            while ((len = reader.read(buff)) != -1) {
                sb.append(buff, 0, len);
            }
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    @PostMapping("/wxpaynotify")
    @ApiOperation(value = "微信支付回调通知处理")
    @IgnoreAuth
    public String handleWxPayNotify() throws WxPayException {
//        String xmlData = this.getStringRequest();
//        WxPayOrderNotifyResult notifyResult = wxService.parseOrderNotifyResult(xmlData);
//        if (!notifyResult.getReturnCode().equals(WxPayConstants.ResultCode.SUCCESS))
//            return WxPayNotifyResponse.success("错误已处理");
//
//        return appService.handleWxPayNotify(new OrderPayResultRequest(notifyResult));
        return null;
    }

}
