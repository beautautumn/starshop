package com.stardata.starshop2.valueadded.shopcontext.north.remote;

import com.stardata.starshop2.valueadded.sharedcontext.pl.GeoLocation;
import com.stardata.starshop2.valueadded.shopcontext.pl.*;
import lombok.AllArgsConstructor;

import java.util.List;


/**
 * @author Samson Shu
 * @version 1.0
 * @date 2022/2/22 23:20
 */

@AllArgsConstructor
public class ShopController {

    public ShopResponse create(CreatingShopRequest request) {
        return null;
    }

    public List<ShopResponse> getManageableList(String userId) {
        return null;
    }

    public ShopResponse getById(String shopId) {
        return null;
    }

    public void save(UpdatingShopRequest request) {

    }

    public OpenTimeResponse getOpeningTime(String shopId) {
        return null;
    }

    public void updateOpeningTime(UpdatingOpeningTimeRequest request) {

    }

    public ShopResponse current(String userId, GeoLocation location) {
        return null;
    }

    public List<ShopResponse> lastHistory(String userId) {
        return null;
    }

    public List<ShopResponse> searchByKeyword(String keyword) {
        return null;
    }

    public List<ShopResponse> searchBrand(String referShopId, String keyword, GeoLocation location) {
        return null;
    }

    public void joinToBrand(String shopId, String brandShopId) {

    }

    public void quitBrand(String shopId, String brandShopId) {

    }


}
