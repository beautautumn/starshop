package com.stardata.starshop2.valueadded.productcontext.query.resource;

import com.stardata.starshop2.valueadded.productcontext.pl.CategoryWithProductsResponse;
import com.stardata.starshop2.valueadded.productcontext.pl.ProductResponse;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/6 12:41
 */
@Api(tags = "商品查询类资源接口")
@RestController
@RequestMapping("/v2/shops/{shopId}")
@AllArgsConstructor
public class ProductQueryResource {
    @GetMapping("/products_by_categories")
    public ResponseEntity<List<CategoryWithProductsResponse>> queryProductCategoriesOnShelves(@PathVariable Long shopId) {
        return null;
    }

    @GetMapping("/products_by_keyword")
    public ResponseEntity<List<ProductResponse>> queryProductsOnShelves(@PathVariable Long shopId, String keyword) {
        return null;
    }
}
