package org.wallet.common.entity.admin;

import lombok.Getter;
import lombok.Setter;
import org.wallet.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 角色用户关系
 *
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "sys_role_user")
public class SysRoleUserEntity extends BaseEntity {

	/**
	 * 角色ID
	 */
	@Column(columnDefinition = "bigint(19) not null comment '角色ID'")
	private Long roleId;
	/**
	 * 用户ID
	 */
	@Column(columnDefinition = "bigint(19) not null comment '用户ID'")
	private Long userId;

}
