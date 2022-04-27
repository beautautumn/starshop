package com.stardata.starshop2.valueadded.ordercontext.command.north.remote;

import com.stardata.starshop2.valueadded.ordercontext.command.north.local.ShoppingCartAppService;
import com.stardata.starshop2.valueadded.ordercontext.command.pl.ShoppingCartRequest;
import com.stardata.starshop2.valueadded.ordercontext.command.pl.ShoppingCartResponse;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/26 17:49
 */
@Api(tags = "购物车资源接口")
@RestController
@RequestMapping("/v2/shoppingcart")
@AllArgsConstructor
public class ShoppingCartResource {
    private final ShoppingCartAppService appService;

    @PutMapping("/{shopId}")
    public ResponseEntity<ShoppingCartResponse> saveShoppingCart(ShoppingCartRequest request) {
        ShoppingCartResponse response = appService.saveShoppingCart(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
