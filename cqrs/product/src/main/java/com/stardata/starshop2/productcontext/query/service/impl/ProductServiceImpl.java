package com.stardata.starshop2.productcontext.query.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stardata.starshop2.productcontext.query.constant.CommonConstants;
import com.stardata.starshop2.productcontext.query.entity.Product;
import com.stardata.starshop2.productcontext.query.entity.ProductCategory;
import com.stardata.starshop2.productcontext.query.entity.ProductImage;
import com.stardata.starshop2.productcontext.query.entity.ProductSaleStat;
import com.stardata.starshop2.productcontext.query.mapper.ProductCategoryMapper;
import com.stardata.starshop2.productcontext.query.mapper.ProductImageMapper;
import com.stardata.starshop2.productcontext.query.mapper.ProductMapper;
import com.stardata.starshop2.productcontext.query.mapper.ProductSaleStatMapper;
import com.stardata.starshop2.productcontext.query.service.ProductService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/7/9 18:38
 */
@Service
@AllArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
        implements ProductService {
    private final ProductCategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    private final ProductSaleStatMapper saleStatMapper;
    private final ProductImageMapper imageMapper;

    /**
     * 处理商品的折扣价格
     * @param product 待处理的商品销售信息对象
     */
    private  void handleProdSaleDiscount(Product product) {
        long originalPriceFen = product.getOriginalPriceFen();
        long discountPriceFen = originalPriceFen;

        String discountType = product.getDiscountType();

        if (CommonConstants.DiscountType.RATE.equals(discountType)) { //按比例折扣
            double discountRate = product.getDiscountRate().doubleValue();

            discountPriceFen = new BigDecimal(originalPriceFen * discountRate)
                    .setScale(0, RoundingMode.HALF_UP).intValue();
        }
        else if (CommonConstants.DiscountType.FIXED.equals(discountType)) { //固定金额
            discountPriceFen = product.getDiscountPriceFen();
        }
        product.setDiscountPriceFen(discountPriceFen);
        product.setDiscounted(discountPriceFen < originalPriceFen);
    }

    /**
     * 私有方法：用于将查询的商品类别、商品销售信息两个 Map List
     * 合并为一个按商品分组的Map List（key 为类别对象、value为商品销售信息对象）
     * @param prodCategories 待合并的商品分类 List
     * @param products 待合并的商品销售信息 List
     * @return 合并后的 Map List
     */
    @NotNull
    private <T extends Product>  List<Map<Map<String, Object>, List<T>>> convertToCatProdMapList(
            List<ProductCategory> prodCategories, List<T> products ) {
        //1 先根据商品分组将最外层的数据结构建立起来
        List<Map<Map<String, Object>, List<T>>> resultList = new ArrayList<>();

        for (ProductCategory prodCategory : prodCategories) {
            Map<String, Object> cat = new HashMap<>();
            cat.put("id", prodCategory.getId());
            cat.put("name", prodCategory.getName());

            Map<Map<String, Object>, List<T>> catMap = new HashMap<>();
            List<T> prodSaleVos = new ArrayList<>();
            catMap.put(cat, prodSaleVos);
            resultList.add(catMap);
        }

        //2 然后对查询出的上架商品列表进行循环，创建 ProductSaleVo 对象，并逐一插入到商品分组下挂的 List 中
        for (T product : products) {
            //2.1 先处理 ProductSaleVo 对象的商品折扣
            handleProdSaleDiscount(product);

            //2.2 找到该商品对应的分组，并将其加入分组下的 List 中
            Long catId = product.getCategoryId();
            for (Map<Map<String, Object>, List<T>> catMap : resultList) {
                Map<String, Object> cat = catMap.keySet().iterator().next();
                List<T> prodSaleVos = catMap.get(cat);
                if (cat.get("id").equals(catId)) {
                    prodSaleVos.add(product);
                }
            }
        }

        //3 去掉那些不含任何商品的商品分类
        for (int i=resultList.size()-1; i>=0; i--) {
            Map<Map<String, Object>, List<T>> catMap = resultList.get(i);
            List<T> prodSaleVos = catMap.values().iterator().next();
            if (prodSaleVos.size()==0) {
                resultList.remove(catMap);
            }
        }

        return resultList;
    }

    @Override
    public List<Map<Map<String, Object>, List<Product>>> queryProdSaleOnShelves(
            @NotNull Long shopId, @NotNull String month) {
        //1. 查询该店对应商品分组
        QueryWrapper<ProductCategory> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.eq("shop_id", shopId).orderByAsc("display_order");
        List<ProductCategory> prodCategories = categoryMapper.selectList(categoryQueryWrapper);

        //2. 查询该店所有上架的商品
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        List<String> orderBy = Arrays.asList("category_id", "display_order");
        productQueryWrapper.eq("shop_id", shopId).eq("on_shelves", "1").orderByAsc(orderBy);
        List<Product> products = productMapper.selectList(productQueryWrapper);

        //3. 为这些商品查询指定月份的销量、图片
        for (Product product : products) {
            QueryWrapper<ProductSaleStat> saleStatQueryWrapper = new QueryWrapper<>();
            saleStatQueryWrapper.eq("product_id", product.getId())
                    .eq("month", month);
            List<ProductSaleStat> saleStats = saleStatMapper.selectList(saleStatQueryWrapper);
            if (saleStats.size()<1) {
                ProductSaleStat zeroStat = new ProductSaleStat();
                zeroStat.setProductId(product.getId());
                zeroStat.setMonth(month);
                zeroStat.setSaleCount(0L);
                product.setSaleStat(zeroStat);
            } else {
                product.setSaleStat(saleStats.get(0));
            }

            QueryWrapper<ProductImage> imageQueryWrapper = new QueryWrapper<>();
            imageQueryWrapper.eq("product_id", product.getId())
                    .orderByAsc("display_order");
            List<ProductImage> images = imageMapper.selectList(imageQueryWrapper);
            product.setImages(images);
            if (images.size()>0) {
                product.setFirstImage(images.get(0).getThumbUrlPath());
            }
        }

        //4. 将查询结果 Map 对象整理成 ProductSaleVo 并同时完成商品分组
        return convertToCatProdMapList(prodCategories, products);
    }
}
