package com.stardata.starshop2.productcontext.query;

import com.stardata.starshop2.productcontext.query.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/7/9 18:06
 */
@SpringBootTest
public class ProductQueryTest {

    @Autowired
    private ProductService productService;

    @Test
    void should_query_product_by_category()  {
        String month = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        List result = productService.queryProdSaleOnShelves(1L, month);
        assertNotNull(result);
    }
}
