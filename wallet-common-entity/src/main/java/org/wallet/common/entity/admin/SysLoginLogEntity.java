package org.wallet.common.entity.admin;

import lombok.Getter;
import lombok.Setter;
import org.wallet.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 登录日志
 *
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "sys_log_login")
public class SysLoginLogEntity extends BaseEntity {

	/**
	 * 用户操作(0：登录|1：登出)
	 */
	@Column(columnDefinition = "tinyint(1) not null comment '用户操作(0：登录|1：登出)'")
	private Integer operation;
	/**
	 * 状态(0：失败|1：成功|2：账号已锁定)
	 */
	@Column(columnDefinition = "tinyint(1) not null comment '状态(0：失败|1：成功|2：锁定)'")
	private Integer status;
	/**
	 * 用户代理
	 */
	@Column(columnDefinition = "varchar(512) not null comment '用户代理'")
	private String userAgent;
	/**
	 * 操作IP
	 */
	@Column(columnDefinition = "varchar(32) not null comment '客户端IP'")
	private String ip;
	/**
	 * 用户名
	 */
	@Column(columnDefinition = "varchar(50) not null comment '用户名'")
	private String creatorName;

}