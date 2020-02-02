package org.wallet.common.dto.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;

import java.util.List;

/**
 * 角色
 * 
 * @author zengfucheng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleDTO extends BaseNormalDTO {
	/**
	 * 部门ID
	 */
	private Long deptId;
    /**
     * 菜单ID列表
     */
    private List<Long> menuIdList;
    /**
     * 部门ID列表 数据权限
     */
    private List<Long> deptIdList;
}
