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
public class ProductImage  implements Serializable {

	@Serial
	private static final long serialVersionUID =  7106143722356679590L;

	private Long id;

	private Long productId;

	private String urlPath;

	private Long displayOrder;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	private String thumbUrlPath;

}
