package org.wallet.common.entity.application;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.wallet.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 主链
 * 
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "app_chain", uniqueConstraints = @UniqueConstraint(name = "uk_app_chain_name", columnNames = "name"))
public class AppChainEntity extends BaseEntity {

	/**
	 * 名称
	 */
	@NotBlank
    @Column(columnDefinition = "varchar(20) not null comment '名称'")
	private String name;

	/**
	 * 币种名称（获取币种行情用，必须跟各大交易所币种名称一致）
	 */
	@NotBlank
    @Column(columnDefinition = "varchar(20) not null comment '币种名称'")
	private String coinName;
    /**
     * 简介
     */
	@NotBlank
    @Column(columnDefinition = "varchar(100) not null comment '简介'")
    private String intro;
    /**
     * 图标
     */
	@NotBlank
    @Column(columnDefinition = "varchar(100) not null comment '图标'")
    private String icon;
	/**
	 * 排序
	 */
    @Column(columnDefinition = "int(4) not null default 0 comment '排序'")
	private Integer sort;
	/**
	 * 是否可用
	 */
    @Column(columnDefinition = "tinyint(1) not null default 1 comment '是否可用'")
	private Boolean enable;
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