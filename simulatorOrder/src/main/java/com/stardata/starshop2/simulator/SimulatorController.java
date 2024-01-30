package com.stardata.starshop2.simulator;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.stardata.starshop2.ordercontext.command.north.local.OrderAppService;
import com.stardata.starshop2.ordercontext.command.pl.OrderConfirmedResponse;
import com.stardata.starshop2.ordercontext.command.pl.OrderPayResultRequest;
import com.stardata.starshop2.ordercontext.command.pl.OrderResponse;
import com.stardata.starshop2.ordercontext.command.pl.OrderSubmitRequest;
import com.stardata.starshop2.sharedcontext.domain.SessionUser;
import com.stardata.starshop2.sharedcontext.north.Resources;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2024/1/30 09:21
 */
@RestController
@RequestMapping("")
@AllArgsConstructor
public class SimulatorController {
    private final OrderAppService appService;

    /**
     * 以下接口是为了支持otel开发，临时开放的相关API
     */
    @PostMapping("/v2/shops/{shopId}/orders_simulator")
    public ResponseEntity<OrderResponse> create(@RequestParam Long userId,
                                                @PathVariable Long shopId, @RequestBody OrderSubmitRequest request)
    {
        SessionUser loginUser = SessionUser.from(userId);

        return Resources.with("create order")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.FORBIDDEN)
                .execute(() -> appService.create(loginUser, shopId, request));
    }

    @PutMapping("/v2/shops/{shopId}/orders_simulator/{orderId}/wxpay_simulator")
    public ResponseEntity<String> simulatePayAndConfirm(
            @RequestParam String orderNumber,
            @RequestParam Integer cashFee)
    {
        return Resources.with("simulate wxpay order")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.FORBIDDEN)
                .execute(() ->  {
                    WxPayOrderNotifyResult wxPayOrderNotifyResult = new WxPayOrderNotifyResult();
                    wxPayOrderNotifyResult.setOutTradeNo(orderNumber);
                    wxPayOrderNotifyResult.setTransactionId("simulator_transaction_id");
                    wxPayOrderNotifyResult.setResultCode("SUCCESS");
                    wxPayOrderNotifyResult.setCashFee(cashFee);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    wxPayOrderNotifyResult.setTimeEnd(LocalDateTime.now().format(formatter));
                    OrderPayResultRequest orderPayResultRequest = new OrderPayResultRequest(wxPayOrderNotifyResult);
                    return appService.handleWxPayNotify(orderPayResultRequest);
                });
    }

    @PutMapping("/v2/shops/{shopId}/orders_simulator/{orderId}/confirmation_simulator")
    public ResponseEntity<OrderConfirmedResponse> simulateConfirmReceived(@PathVariable Long orderId)
    {
        return Resources.with("simulate confirm order received")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.FORBIDDEN)
                .execute(() -> appService.confirmReceived(orderId));
    }

    @PutMapping("/simulator_rate")
    public ResponseEntity<String> setConfig(@RequestParam String key, @RequestParam Double value) {
        return Resources.with("set simulator rate")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.FORBIDDEN)
                .execute(() -> {
                    ExceptionRateConfig.rateValues.put(key, value);
                    return "OK";
                });
    }

    @GetMapping("/simulator_rate")
    public ResponseEntity<String> getConfig(@RequestParam String key) {
        return Resources.with("set simulator rate")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.FORBIDDEN)
                .execute(() -> ExceptionRateConfig.rateValues.get(key)==null?"None":ExceptionRateConfig.rateValues.get(key).toString());
    }
}
