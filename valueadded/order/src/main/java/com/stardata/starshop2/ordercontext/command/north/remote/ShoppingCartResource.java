package com.stardata.starshop2.ordercontext.command.north.remote;

import com.stardata.starshop2.sharedcontext.annotation.LoginUser;
import com.stardata.starshop2.sharedcontext.domain.SessionUser;
import com.stardata.starshop2.ordercontext.command.north.local.ShoppingCartAppService;
import com.stardata.starshop2.ordercontext.command.pl.ShoppingCartRequest;
import com.stardata.starshop2.ordercontext.command.pl.ShoppingCartResponse;
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
@RequestMapping("/v2/shops/{shopId}/shoppingcarts")
@AllArgsConstructor
public class ShoppingCartResource {
    private final ShoppingCartAppService appService;

    @PutMapping("")
    public ResponseEntity<ShoppingCartResponse> save(@LoginUser SessionUser loginUser,
                                                                 @PathVariable Long shopId, ShoppingCartRequest request)
    {
        ShoppingCartResponse response = appService.save(loginUser, shopId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<ShoppingCartResponse> query(@LoginUser SessionUser loginUser, @PathVariable Long shopId)
    {
        ShoppingCartResponse response = appService.query(loginUser.getId().value(), shopId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
