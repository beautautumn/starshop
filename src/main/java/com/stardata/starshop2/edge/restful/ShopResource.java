package com.stardata.starshop2.edge.restful;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Samson Shu
 * @email shush@stardata.top
 * @version 1.0
 * @date 2022/2/22 23:32
 */

@Api(tags = "店铺服务接口")
@RestController
@RequestMapping("/v2/shops")
@AllArgsConstructor
public class ShopResource {
}
