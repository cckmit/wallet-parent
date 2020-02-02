package org.wallet.common.entity.application;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.wallet.common.entity.BaseEntity;
import org.wallet.common.enums.application.AppAdvertPointEnum;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * App 广告
 * 
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "app_advert", indexes = {
        @Index(name = "ind_app_advert_chain_id", columnList = "chainId"),
        @Index(name = "ind_app_advert_point", columnList = "point")
})
public class AppAdvertEntity extends BaseEntity {

    /**
     * 主链ID
     */
    @Column(columnDefinition = "bigint(19) not null comment '主链ID'")
    private Long chainId;
    /**
     * 广告点位（枚举）
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(50) not null comment '广告点位'")
    private AppAdvertPointEnum point;
    /**
     * 简介
     */
    @Column(columnDefinition = "varchar(100) not null comment '简介'")
    private String intro;
    /**
     * App ID
     */
    @Column(columnDefinition = "bigint(19) not null comment 'App ID'")
    private Long appId;
    /**
     * 缩略图
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(100) not null comment '缩略图'")
    private String img;
    /**
     * url
     */
    @Column(columnDefinition = "varchar(100) not null comment 'url'")
    private String url;
    /**
     * 展示时间
     */
    @Min(1)
    @Max(9999)
    @Column(columnDefinition = "int(4) not null default 0 comment '展示时间（秒）'")
    private String showTime;
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