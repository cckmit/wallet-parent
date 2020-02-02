package org.wallet.common.entity.admin;

import lombok.Getter;
import lombok.Setter;
import org.wallet.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 操作日志
 *
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "sys_log_operation")
public class SysOperationLogEntity extends BaseEntity {

	/**
	 * 用户操作
	 */
	@Column(columnDefinition = "varchar(50) not null comment '用户操作'")
	private String operation;
	/**
	 * 请求URI
	 */
	@Column(columnDefinition = "varchar(200) not null comment '请求URI'")
	private String requestUri;
	/**
	 * 请求方式
	 */
	@Column(columnDefinition = "varchar(10) not null comment '请求方式'")
	private String requestMethod;
	/**
	 * 请求参数
	 */
	@Column(columnDefinition = "text not null comment '请求参数'")
	private String requestParams;
	/**
	 * 请求时长(毫秒)
	 */
	@Column(columnDefinition = "int(6) not null comment '请求时长(毫秒)'")
	private Integer requestTime;
	/**
	 * 响应结果
	 */
	@Column(columnDefinition = "text not null comment '响应结果'")
	private String responseBody;
	/**
	 * 用户代理
	 */
	@Column(columnDefinition = "varchar(512) not null comment '用户代理'")
	private String userAgent;
	/**
	 * 操作IP
	 */
	@Column(columnDefinition = "varchar(32) not null comment '操作IP'")
	private String ip;
	/**
	 * 状态(0：成功|-1：业务失败|-2：异常)
	 */
	@Column(columnDefinition = "tinyint(1) not null comment '状态(0：成功|-1：业务失败|-2：异常)'")
	private Integer status;
	/**
	 * 用户名
	 */
	@Column(columnDefinition = "varchar(50) not null comment '用户名'")
	private String creatorName;
}