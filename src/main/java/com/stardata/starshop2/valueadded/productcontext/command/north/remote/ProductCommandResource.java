package com.stardata.starshop2.valueadded.productcontext.command.north.remote;

import com.stardata.starshop2.sharedcontext.annotation.LoginUser;
import com.stardata.starshop2.sharedcontext.pl.SessionUser;
import com.stardata.starshop2.valueadded.ordercontext.command.north.local.OrderAppService;
import com.stardata.starshop2.valueadded.ordercontext.pl.OrderResponse;
import com.stardata.starshop2.valueadded.productcontext.command.north.local.ProductAppService;
import com.stardata.starshop2.valueadded.productcontext.pl.ProductResponse;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/2/28 14:25
 */
@Api(tags = "商品命令类资源接口")
@RestController
@RequestMapping("/v2/shops/{shopId}/products")
@AllArgsConstructor
public class ProductCommandResource {
    private final ProductAppService appService;
    
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getDetail(@LoginUser SessionUser loginUser,
                                                   @PathVariable Long productId)
    {
        ProductResponse response = appService.getDetail(productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
