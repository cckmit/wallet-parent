package org.wallet.common.entity.admin;

import lombok.Getter;
import lombok.Setter;
import org.wallet.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 异常日志
 *
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "sys_log_error")
public class SysErrorLogEntity extends BaseEntity {

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
	 * 异常信息
	 */
	@Column(columnDefinition = "text not null comment '异常信息'")
	private String errorInfo;

}