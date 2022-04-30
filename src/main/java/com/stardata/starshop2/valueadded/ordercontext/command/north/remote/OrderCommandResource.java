package com.stardata.starshop2.valueadded.ordercontext.command.north.remote;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.stardata.starshop2.sharedcontext.annotation.IgnoreAuth;
import com.stardata.starshop2.sharedcontext.annotation.LoginUser;
import com.stardata.starshop2.sharedcontext.pl.SessionUser;
import com.stardata.starshop2.valueadded.ordercontext.command.north.local.OrderAppService;
import com.stardata.starshop2.valueadded.ordercontext.pl.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/v2/orders")
@AllArgsConstructor
public class OrderCommandResource {

    @Autowired
    private HttpServletRequest request;

    private final WxPayService wxService;

    private final OrderAppService appService;

    @PostMapping("/{shopId}")
    public ResponseEntity<OrderResponse> create(@LoginUser SessionUser loginUser,
                                                @PathVariable Long shopId, OrderSubmitRequest request)
    {
        OrderResponse response = appService.create(loginUser, shopId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{shopId}/{orderId}/prepay")
    public ResponseEntity<PrepayResponse> prepay(@LoginUser SessionUser loginUser,
                                                 @PathVariable Long shopId,
                                                 @PathVariable Long orderId)
    {
        PrepayResponse response = appService.prepay(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{shopId}/{orderId}")
    public ResponseEntity<OrderResponse> getDetail(@LoginUser SessionUser loginUser,
                                                   @PathVariable Long shopId,
                                                   @PathVariable Long orderId)
    {
        OrderResponse response = appService.getDetail(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

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
        String xmlData = this.getStringRequest();
        WxPayOrderNotifyResult notifyResult = wxService.parseOrderNotifyResult(xmlData);
        if (!notifyResult.getReturnCode().equals(WxPayConstants.ResultCode.SUCCESS))
            return WxPayNotifyResponse.success("错误已处理");

        return appService.handleWxPayNotify(new OrderPayResultRequest(notifyResult));
    }


    @PutMapping("/{shopId}/{orderId}/confirmation")
    public ResponseEntity<OrderConfirmedResponse> confirmReceived(@LoginUser SessionUser loginUser,
                                                                  @PathVariable Long shopId,
                                                                  @PathVariable Long orderId)
    {
        OrderConfirmedResponse response = appService.confirmReceived(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{shopId}/{orderId}")
    public ResponseEntity<OrderDeletedResponse> delete(@LoginUser SessionUser loginUser,
                                                         @PathVariable Long shopId,
                                                         @PathVariable Long orderId)
    {
        OrderDeletedResponse response = appService.delete(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
