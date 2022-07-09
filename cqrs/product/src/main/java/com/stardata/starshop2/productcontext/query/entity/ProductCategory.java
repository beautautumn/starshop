package com.stardata.starshop2.productcontext.query.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author  Samson Shu
 * @email shush@stardata.top
 * @date 2022/07/09 
 */

@Data
@TableName("tb_prod_category")
public class ProductCategory implements Serializable {

	@Serial
	private static final long serialVersionUID =  860154376245995461L;

	private Long id;

	private Long shopId;

	private String name;

	/**
	 * 显示顺序从小到大排列
	 */
	private Long displayOrder;

	/**
	 * 0：记录无效；1：记录有效。
	 */
	private String isValid;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

}
