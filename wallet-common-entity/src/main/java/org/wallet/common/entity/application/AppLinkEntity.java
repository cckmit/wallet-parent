package org.wallet.common.entity.application;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.wallet.common.entity.BaseEntity;
import org.wallet.common.enums.application.AppLinkTypeEnum;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * App 超链
 * 
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "app_link")
public class AppLinkEntity extends BaseEntity {

    /**
     * 类型
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) not null comment '类型'")
    private AppLinkTypeEnum type;
    /**
     * 名称
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(20) not null comment '名称'")
    private String name;
    /**
     * 地址
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(256) not null comment '地址'")
    private String url;
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
     * 是否可删除
     */
    @Column(columnDefinition = "tinyint(1) not null default 1 comment '是否可删除'")
    private Boolean deletable;
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