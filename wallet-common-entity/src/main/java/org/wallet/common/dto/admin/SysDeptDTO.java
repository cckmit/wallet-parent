package org.wallet.common.dto.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;
import org.wallet.common.dto.SearchOperator;
import org.wallet.common.dto.SearchProperty;

/**
 * 部门
 * 
 * @author zengfucheng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptDTO extends BaseNormalDTO {
	/**
	 * ID
	 */
	private Long id;
	/**
	 * 上级ID
	 */
	private Long pid;
	/**
	 * 所有上级ID，用逗号分开
	 */
	private String pids;
	/**
	 * 部门名称
	 */
	@SearchProperty(SearchOperator.allLike)
	private String name;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 上级部门名称
	 */
	private String parentName;

}