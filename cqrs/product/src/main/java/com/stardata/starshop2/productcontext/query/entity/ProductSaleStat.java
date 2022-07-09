package com.stardata.starshop2.productcontext.query.entity;

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
public class ProductSaleStat  implements Serializable {

	@Serial
	private static final long serialVersionUID =  2675561165939383345L;

	private Long id;

	private Long productId;

	/**
	 * 格式：YYYYMM
	 */
	private String month;

	private Long saleCount;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

}
