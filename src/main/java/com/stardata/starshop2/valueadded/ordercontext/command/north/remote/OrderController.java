package com.stardata.starshop2.valueadded.ordercontext.command.north.remote;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.stardata.starshop2.sharedcontext.annotation.IgnoreAuth;
import com.stardata.starshop2.valueadded.ordercontext.command.north.local.OrderAppService;
import com.stardata.starshop2.valueadded.ordercontext.pl.OrderPayResultRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/30 16:51
 */
@Api(tags = "订单控制器接口")
@AllArgsConstructor
@Service
public class OrderController {

    private final OrderAppService appService;

    public void autoConfirm() {
        appService.autoConfirm();
    }


    public void autoCancel() {
        appService.autoCancel();
    }
}
