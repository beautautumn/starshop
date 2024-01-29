package com.stardata.starshop2.ordercontext.command.north.remote;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.stardata.starshop2.ordercontext.command.north.local.OrderAppService;
import com.stardata.starshop2.ordercontext.command.pl.*;
import com.stardata.starshop2.sharedcontext.annotation.LoginUser;
import com.stardata.starshop2.sharedcontext.domain.SessionUser;
import com.stardata.starshop2.sharedcontext.north.Resources;
import io.swagger.annotations.Api;
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
 * @date 2022/4/29 13:53
 */
@Api(tags = "订单命令类资源接口")
@RestController
@RequestMapping("/v2/shops/{shopId}/orders")
@AllArgsConstructor
public class OrderCommandResource {

    private final OrderAppService appService;

    @PostMapping("")
    public ResponseEntity<OrderResponse> create(@LoginUser SessionUser loginUser,
                                                @PathVariable Long shopId, OrderSubmitRequest request)
    {
        return Resources.with("create order")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.FORBIDDEN)
                .execute(() -> appService.create(loginUser, shopId, request));
    }

    @PostMapping("/{orderId}/prepayments")
    public ResponseEntity<PrepayOrderResponse> prepay(@LoginUser SessionUser loginUser,
                                                      @PathVariable Long orderId)
    {
        return Resources.with("prepay order")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.FORBIDDEN)
                .execute(() -> appService.prepay(orderId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getDetail(@LoginUser SessionUser loginUser,
                                                   @PathVariable Long orderId)
    {
        return Resources.with("get order detail info")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.FORBIDDEN)
                .execute(() -> appService.getDetail(orderId));

    }

    @PutMapping("/{orderId}/confirmation")
    public ResponseEntity<OrderConfirmedResponse> confirmReceived(@LoginUser SessionUser loginUser,
                                                                  @PathVariable Long orderId)
    {
        return Resources.with("confirm order received")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.FORBIDDEN)
                .execute(() -> appService.confirmReceived(orderId));
    }

    @PutMapping("/{orderId}/invisible_setting")
    public ResponseEntity<OrderDeletedResponse> delete(@LoginUser SessionUser loginUser,
                                                       @PathVariable Long orderId)
    {
        return Resources.with("delete order")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.FORBIDDEN)
                .execute(() -> appService.delete(orderId));
    }

    /**
     * 以下接口是为了支持otel开发，临时开放的相关API
     */
    @PostMapping("/byUserId")
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

    @PutMapping("/{orderId}/wxpay_simulator")
    public ResponseEntity<String> simulatePayAndConfirm(
            @RequestParam Long userId,
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

    @PutMapping("/{orderId}/confirmation_simulator")
    public ResponseEntity<OrderConfirmedResponse> confirmReceived(@RequestParam Long userId,
                                                                  @PathVariable Long orderId)
    {
        return Resources.with("confirm order received")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.FORBIDDEN)
                .execute(() -> appService.confirmReceived(orderId));
    }


}
