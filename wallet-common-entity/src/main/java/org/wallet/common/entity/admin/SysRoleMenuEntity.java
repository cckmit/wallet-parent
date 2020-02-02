package org.wallet.common.entity.admin;

import lombok.Getter;
import lombok.Setter;
import org.wallet.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 角色菜单关系
 *
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "sys_role_menu")
public class SysRoleMenuEntity extends BaseEntity {

	/**
	 * 角色ID
	 */
	@Column(columnDefinition = "bigint(19) not null comment '角色ID'")
	private Long roleId;
	/**
	 * 菜单ID
	 */
	@Column(columnDefinition = "bigint(19) not null comment '菜单ID'")
	private Long menuId;

}
