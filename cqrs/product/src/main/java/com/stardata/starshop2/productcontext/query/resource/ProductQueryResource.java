package com.stardata.starshop2.productcontext.query.resource;

import com.stardata.starshop2.productcontext.query.entity.Product;
import com.stardata.starshop2.productcontext.query.service.ProductService;
import com.stardata.starshop2.sharedcontext.north.Resources;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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

    private final ProductService productService;

    @GetMapping("/products_by_categories")
    public ResponseEntity<List<Map<Map<String, Object>, List<Product>>>> queryProductCategoriesOnShelves(@PathVariable Long shopId) {
        String month = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        return Resources.with("query products on shelves by shopId")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.FORBIDDEN)
                .execute(() -> productService.queryProdSaleOnShelves(shopId, month));
    }

    @GetMapping("/products_by_keyword")
    public ResponseEntity<List<Product>> queryProductsOnShelves(@PathVariable Long shopId, String keyword) {
        return null;
    }
}
