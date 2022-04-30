package com.stardata.starshop2.valueadded.ordercontext.query.resource;

import com.stardata.starshop2.sharedcontext.annotation.LoginUser;
import com.stardata.starshop2.sharedcontext.pl.SessionUser;
import com.stardata.starshop2.valueadded.ordercontext.pl.OrderResponse;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/30 16:30
 */
@Api(tags = "订单查询类资源接口")
@RestController
@RequestMapping("/v2/myorders")
@AllArgsConstructor
public class OrderQueryResource {
    @GetMapping("/{shopId}/recent")
    public ResponseEntity<List<OrderResponse>> listForUser(@LoginUser SessionUser loginUser,
                                                           @PathVariable String shopId,
                                                           int status)
    {
        return null;
    }

    @GetMapping("/{shopId}/history")
    public ResponseEntity<List<OrderResponse>> listHistoryForUser(@LoginUser SessionUser loginUser,
                                                           @PathVariable String shopId,
                                                           int status, int pageNo)
    {
        return null;
    }
}
