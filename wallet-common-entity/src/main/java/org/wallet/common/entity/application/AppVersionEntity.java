package org.wallet.common.entity.application;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.wallet.common.entity.BaseEntity;
import org.wallet.common.enums.Device;
import org.wallet.common.enums.application.AppUpgradeStrategyEnum;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * App 版本
 * 
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "app_version", uniqueConstraints = @UniqueConstraint(name = "uk_app_version_device", columnNames = "device"))
public class AppVersionEntity extends BaseEntity {

    /**
     * 平台
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) not null comment '平台'")
    private Device device;
    /**
     * 最高版本号
     */
    @NotNull
    @Column(columnDefinition = "int(5) not null comment '最高版本号'")
    private Integer highestVersion;
    /**
     * 最低版本号
     */
    @NotNull
    @Column(columnDefinition = "int(5) not null comment '最低版本号'")
    private Integer lowestVersion;
    /**
     * 当前版本号
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(20) not null comment '当前版本号'")
    private String currentVersion;
    /**
     * 升级策略
     */
    @NotNull
    @Column(columnDefinition = "varchar(20) not null comment '升级策略'")
    private AppUpgradeStrategyEnum upgradeStrategy;
    /**
     * 升级地址
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(256) not null comment '升级地址'")
    private String url;
    /**
     * 文件大小
     */
    @NotNull
    @Column(columnDefinition = "numeric(10,2) not null comment '文件大小'")
    private Double fileSize;
    /**
     * 版本简介
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(1024) not null comment '版本简介'")
    private String intro;
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