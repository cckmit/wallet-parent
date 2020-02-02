package org.wallet.common.entity.admin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.wallet.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 角色
 * 
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "sys_role")
public class SysRoleEntity extends BaseEntity {

	/**
	 * 角色名称
	 */
	@NotBlank
	@Column(columnDefinition = "varchar(50) not null comment '角色名称'")
	private String name;
	/**
	 * 备注
	 */
	@Column(columnDefinition = "varchar(255) not null comment '备注'")
	private String remark;
	/**
	 * 部门ID
	 */
	@Column(columnDefinition = "bigint(19) not null comment '部门ID'")
	private Long deptId;
	/**
	 * 更新者
	 */
	@Column(columnDefinition = "bigint(19) not null comment '更新者'")
	private Long updater;
	/**
	 * 更新时间
	 */
	@LastModifiedDate
	@Column(columnDefinition = "datetime not null default current_timestamp comment '更新时间'")
	private Date updateDate;
}
