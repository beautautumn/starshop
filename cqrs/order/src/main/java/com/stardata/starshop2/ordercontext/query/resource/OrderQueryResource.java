package com.stardata.starshop2.ordercontext.query.resource;

import com.stardata.starshop2.sharedcontext.annotation.LoginUser;
import com.stardata.starshop2.sharedcontext.pl.SessionUser;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/v2/my")
@AllArgsConstructor
public class OrderQueryResource {
    @GetMapping("/orders")
    public ResponseEntity<List> listForUser(@LoginUser SessionUser loginUser,
                                                           boolean history,
                                                           int status, int pageNo)
    {
        return null;
    }

}
