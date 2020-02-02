package org.wallet.common.entity.admin;

import lombok.Getter;
import lombok.Setter;
import org.wallet.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 角色数据权限
 *
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "sys_role_data_scope")
public class SysRoleDataScopeEntity extends BaseEntity {

	/**
	 * 角色ID
	 */
	@Column(columnDefinition = "bigint(19) not null comment '角色ID'")
	private Long roleId;
	/**
	 * 部门ID
	 */
	@Column(columnDefinition = "bigint(19) not null comment '部门ID'")
	private Long deptId;

}