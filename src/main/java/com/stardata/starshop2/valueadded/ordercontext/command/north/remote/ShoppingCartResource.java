package com.stardata.starshop2.valueadded.ordercontext.command.north.remote;

import com.stardata.starshop2.sharedcontext.annotation.LoginUser;
import com.stardata.starshop2.sharedcontext.pl.SessionUser;
import com.stardata.starshop2.valueadded.ordercontext.command.north.local.ShoppingCartAppService;
import com.stardata.starshop2.valueadded.ordercontext.pl.ShoppingCartRequest;
import com.stardata.starshop2.valueadded.ordercontext.pl.ShoppingCartResponse;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/26 17:49
 */
@Api(tags = "购物车资源接口")
@RestController
@RequestMapping("/v2/shoppingcarts")
@AllArgsConstructor
public class ShoppingCartResource {
    private final ShoppingCartAppService appService;

    @PutMapping("/{shopId}")
    public ResponseEntity<ShoppingCartResponse> save(@LoginUser SessionUser loginUser,
                                                                 @PathVariable String shopId, ShoppingCartRequest request)
    {
        ShoppingCartResponse response = appService.save(loginUser, shopId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{shopId}")
    public ResponseEntity<ShoppingCartResponse> query(@LoginUser SessionUser loginUser, @PathVariable String shopId)
    {
        ShoppingCartResponse response = appService.query(loginUser.getId(), shopId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
