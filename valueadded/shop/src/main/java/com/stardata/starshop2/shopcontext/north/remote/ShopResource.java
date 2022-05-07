package com.stardata.starshop2.shopcontext.north.remote;

import com.stardata.starshop2.sharedcontext.annotation.LoginUser;
import com.stardata.starshop2.sharedcontext.domain.Location;
import com.stardata.starshop2.sharedcontext.pl.SessionUser;
import com.stardata.starshop2.shopcontext.north.local.ShopAppService;
import com.stardata.starshop2.shopcontext.pl.*;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Samson Shu
 * @version 1.0
 * @date 2022/2/22 23:20
 */
@Api(tags = "店铺资源接口")
@RestController
@RequestMapping("/v2/shops")
@AllArgsConstructor

public class ShopResource {
    private final ShopAppService appService;

    @PostMapping("")
    public ResponseEntity<ShopResponse> create(CreatingShopRequest request) {
        //todo 完成创建店铺资源方法
        return null;
    }

    public ResponseEntity<List<ShopResponse>> getManageableList(String userId) {
        //todo 完成获取可管理店铺资源方法
        return null;
    }

    @GetMapping("/{shopId}")
    public ResponseEntity<ShopResponse> getDetail(@LoginUser SessionUser loginUser,
                                                   @PathVariable Long shopId)
    {
        ShopResponse response = appService.getDetail(shopId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{shopId}")
    public void save(UpdatingShopRequest request) {

    }

    @GetMapping("/{shopId}/opening_time")
    public ResponseEntity<OpenTimeResponse> getOpeningTime(String shopId) {
        return null;
    }

    public void updateOpeningTime(UpdatingOpeningTimeRequest request) {

    }

    @GetMapping("/current_for_user")
    public ResponseEntity<ShopResponse> current(String userId, Location location) {
        return null;
    }

    @GetMapping("/histories_for_user")
    public ResponseEntity<List<ShopResponse>> lastHistory(String userId) {
        return null;
    }

    @GetMapping("/list_by_keyword")
    public ResponseEntity<List<ShopResponse>> searchByKeyword(String keyword) {
        return null;
    }

    @GetMapping("/brands_by_keyword")
    public ResponseEntity<List<ShopResponse>> searchBrand(String referShopId, String keyword, Location location) {
        return null;
    }

    @PutMapping("/{shopId}/actions/join_to_brand")
    public void joinToBrand(String shopId, String brandShopId) {

    }

    @PutMapping("/{shopId}/actions/quit_from_brand")
    public void quitFromBrand(String shopId, String brandShopId) {

    }


}
