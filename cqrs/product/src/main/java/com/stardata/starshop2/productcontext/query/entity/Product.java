package com.stardata.starshop2.productcontext.query.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author  Samson Shu
 * @email shush@stardata.top
 * @date 2022/07/09 
 */

@Data
@TableName("tb_product2")
public class Product implements Serializable {
	private Long id;
	private Long categoryId;
	private Long shopId;
	private String name;
	private String introduction;
	private String unit;
	private BigDecimal minPurchase;
	private Long originalPriceFen;

	/**
	 * 显示顺序从小到大排列
	 */
	private Long displayOrder;

	/**
	 * json字符串格式保存标签。例： ["特价优惠", "51庆典"]
	 */
	private String labels;

	/**
	 * 0：售罄；1：有货。
	 */
	private String isAvailable;

	/**
	 * 0：已下架；1：已上架；
	 */
	private String onShelves;

	/**
	 * 1：按比例折扣；2：固定金额。
	 */
	private String discountType;

	private BigDecimal discountRate;

	private Long discountPriceFen;

	private Long purchaseLimit;

	/**
	 * 0：无效记录（被删除）；1：有效记录。
	 */
	private String isValid;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	//是否有优惠（冗余字段，根据优惠后价格和原价对比，算出来的）
	@TableField(exist=false)
	private Boolean discounted;

	@TableField(exist=false)
	private List<ProductImage> images;

	@TableField(exist=false)
	private String firstImage;

	@TableField(exist=false)
	private ProductSaleStat saleStat;

}
