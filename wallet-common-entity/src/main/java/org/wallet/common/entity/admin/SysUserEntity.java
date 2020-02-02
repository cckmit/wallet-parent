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
 * 系统用户
 * 
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "sys_user")
public class SysUserEntity extends BaseEntity {
	/**
	 * 用户名
	 */
	@NotBlank
	@Column(columnDefinition = "varchar(50) not null comment '用户名'")
	private String username;
	/**
	 * 密码
	 */
    @NotBlank
	@Column(columnDefinition = "varchar(100) not null comment '密码'")
	private String password;
	/**
	 * 姓名
	 */
    @NotBlank
	@Column(columnDefinition = "varchar(10) not null comment '姓名'")
	private String realName;
	/**
	 * 头像
	 */
	@Column(columnDefinition = "varchar(200) comment '头像'")
	private String headUrl;
	/**
	 * 性别(0：男|1：女|2：保密)
	 */
	@Column(columnDefinition = "tinyint(1) not null default '2' comment '性别(0：男|1：女|2：保密)'")
	private Integer gender;
	/**
	 * 邮箱
	 */
	@Column(columnDefinition = "varchar(100) comment '邮箱'")
	private String email;
	/**
	 * 手机号
	 */
    @NotBlank
	@Column(columnDefinition = "varchar(20) not null comment '手机号'")
	private String mobile;
	/**
	 * 部门ID
	 */
	@Column(columnDefinition = "bigint(19) not null comment '部门ID'")
	private Long deptId;
	/**
	 * 超级管理员(0：否|1：是)
	 */
	@Column(columnDefinition = "tinyint(1) not null default '0' comment '超级管理员(0：否|1：是)'")
	private Boolean superAdmin;
	/**
	 * 状态(0：停用|1：正常)
	 */
	@Column(columnDefinition = "tinyint(1) not null default '1' comment '状态(0：停用|1：正常)'")
	private Integer status;
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