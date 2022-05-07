package com.stardata.starshop2.platintegrationcontext.resource;

import com.stardata.starshop2.sharedcontext.annotation.LoginUser;
import com.stardata.starshop2.sharedcontext.pl.SessionUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/30 23:34
 */
@Api(tags = "平台集成资源接口")
@RestController
@RequestMapping("/v2/platform")
@AllArgsConstructor
public class PlatformResource {

}
