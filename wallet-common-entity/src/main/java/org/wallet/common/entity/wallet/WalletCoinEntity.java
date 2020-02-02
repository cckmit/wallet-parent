package org.wallet.common.entity.wallet;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.wallet.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 币种
 *
 * @author zengfucheng
 **/
@Getter
@Setter
@Entity
@Table(name = "wallet_coin", uniqueConstraints = @UniqueConstraint(name = "uk_wallet_coin", columnNames = {"chainId", "name"}))
public class WalletCoinEntity extends BaseEntity {
    /**
     * 主链ID
     */
    @NotNull
    @Column(columnDefinition = "bigint(19) not null comment '主链ID'")
    private Long chainId;
    /**
     * 商标
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(255) not null comment 'logo'")
    private String logo;
    /**
     * 名称
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(20) not null comment '名称'")
    private String name;
    /**
     * 全称
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(50) not null comment '全称'")
    private String fullName;
    /**
     * 合约地址
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(64) not null comment '合约地址'")
    private String contractAddress;
    /**
     * 小数点位数
     */
    @Column(columnDefinition = "int(2) not null comment '小数点位数'")
    private Integer decimals;
    /**
     * 项目名称
     */
    @Column(columnDefinition = "varchar(50) default null comment '项目名称'")
    private String projectName;
    /**
     * 项目官网
     */
    @Column(columnDefinition = "varchar(255) default null comment '项目官网'")
    private String projectWebsite;
    /**
     * 项目简介
     */
    @Column(columnDefinition = "varchar(255) default null comment '项目简介'")
    private String projectIntro;
    /**
     * 三方评级
     */
    @Column(columnDefinition = "varchar(255) default null comment '三方评级'")
    private String thirdRating;
    /**
     * 发行时间
     */
    @Column(columnDefinition = "datetime default null comment '发行时间'")
    private Date releaseTime;
    /**
     * 发行总量
     */
    @Column(columnDefinition = "varchar(50) default null comment '发行总量'")
    private String releaseTotalAmount;
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
