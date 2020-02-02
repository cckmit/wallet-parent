package org.wallet.common.entity.application;

import lombok.Getter;
import lombok.Setter;
import org.wallet.common.entity.BaseEntity;
import org.wallet.common.enums.application.AppTagEnum;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * App 标签
 * 
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "app_tag", uniqueConstraints = @UniqueConstraint(name = "uk_app_tag", columnNames = {"chainId", "typeId", "appId", "tag"}))
public class AppTagEntity extends BaseEntity {
	/**
	 * 主链ID
	 */
	@Column(columnDefinition = "bigint(19) not null comment '主链ID'")
	private Long chainId;
	/**
	 * 类型ID
	 */
	@Column(columnDefinition = "bigint(19) not null comment '类型ID'")
	private Long typeId;
	/**
	 * App ID
	 */
	@NotNull
    @Column(columnDefinition = "bigint(19) not null comment 'App ID'")
	private Long appId;
    /**
     * 标签（枚举）
     */
    @NotNull
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(20) not null comment '标签'")
    private AppTagEnum tag;
	/**
	 * 图片
	 */
	@Column(columnDefinition = "varchar(100) not null comment '图片'")
	private String img;
	/**
	 * 排序
	 */
    @Column(columnDefinition = "int(4) not null default 0 comment '排序'")
	private Integer sort;

}