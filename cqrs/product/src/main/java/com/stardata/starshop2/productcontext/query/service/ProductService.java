package com.stardata.starshop2.productcontext.query.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stardata.starshop2.productcontext.query.entity.Product;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/7/9 18:34
 */
public interface ProductService extends IService<Product> {
    /**
     * 查询指定 shopId 的已上架商品目录，按照商品分类进行分组。
     * 其返回结构为：
     * 1. 最外层数据： List对象表示的 n 个 Map， 也就是最终返回结果；
     * 2. 第二层数据：每个 Map 对象，表示一个商品分类（按显示顺序排列），具体内容为：
     *      key 表示分类（用一个map表达，含分类 id 和 name），
     *      value 为另一个 List 对象，用来存放商品信息
     * 3. 第三层数据：每个 List 对象，是 ProductSaleVo 商品实体对象的列表（按显示顺序排列）
     * 4. ProductSaleVo 是一个专门用来记录商品销售相关信息的实体对象，其综合了商品表、商品销售关系表、图片路径表的相关信息
     *
     * @param shopId 店铺ID
     * @return List<Map<Map<String, Object>, List<ProductSaleVo>>> 店铺在售商品列表
     */
    List<Map<Map<String, Object>, List<Product>>> queryProdSaleOnShelves
            (@NotNull Long shopId, @NotNull String month);
}
