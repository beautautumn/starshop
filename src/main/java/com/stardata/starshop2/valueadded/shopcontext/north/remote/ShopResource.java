package com.stardata.starshop2.valueadded.shopcontext.north.remote;

import com.stardata.starshop2.sharedcontext.domain.Location;
import com.stardata.starshop2.valueadded.shopcontext.pl.*;
import lombok.AllArgsConstructor;

import java.util.List;


/**
 * @author Samson Shu
 * @version 1.0
 * @date 2022/2/22 23:20
 */

@AllArgsConstructor
public class ShopResource {

    public ShopResponse create(CreatingShopRequest request) {
        //todo 完成创建店铺资源方法
        return null;
    }

    public List<ShopResponse> getManageableList(String userId) {
        //todo 完成获取可管理店铺资源方法
        return null;
    }

    public ShopResponse getById(String shopId) {
        //todo 完成根据店铺ID获取店铺信息资源方法
        return null;
    }

    public void save(UpdatingShopRequest request) {

    }

    public OpenTimeResponse getOpeningTime(String shopId) {
        return null;
    }

    public void updateOpeningTime(UpdatingOpeningTimeRequest request) {

    }

    public ShopResponse current(String userId, Location location) {
        return null;
    }

    public List<ShopResponse> lastHistory(String userId) {
        return null;
    }

    public List<ShopResponse> searchByKeyword(String keyword) {
        return null;
    }

    public List<ShopResponse> searchBrand(String referShopId, String keyword, Location location) {
        return null;
    }

    public void joinToBrand(String shopId, String brandShopId) {

    }

    public void quitBrand(String shopId, String brandShopId) {

    }


}
