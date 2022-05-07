package com.stardata.starshop2.ordercontext.command.north.remote;

import com.stardata.starshop2.ordercontext.command.north.local.OrderAppService;
import com.stardata.starshop2.ordercontext.command.pl.*;
import com.stardata.starshop2.sharedcontext.annotation.LoginUser;
import com.stardata.starshop2.sharedcontext.pl.SessionUser;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        OrderResponse response = appService.create(loginUser, shopId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getDetail(@LoginUser SessionUser loginUser,
                                                   @PathVariable Long orderId)
    {
        OrderResponse response = appService.getDetail(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{orderId}/prepayments")
    public ResponseEntity<PrepayResponse> prepay(@LoginUser SessionUser loginUser,
                                                 @PathVariable Long orderId)
    {
        PrepayResponse response = appService.prepay(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{orderId}/confirmation")
    public ResponseEntity<OrderConfirmedResponse> confirmReceived(@LoginUser SessionUser loginUser,
                                                                  @PathVariable Long orderId)
    {
        OrderConfirmedResponse response = appService.confirmReceived(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{orderId}/invisible_setting")
    public ResponseEntity<OrderDeletedResponse> delete(@LoginUser SessionUser loginUser,
                                                       @PathVariable Long orderId)
    {
        OrderDeletedResponse response = appService.delete(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
