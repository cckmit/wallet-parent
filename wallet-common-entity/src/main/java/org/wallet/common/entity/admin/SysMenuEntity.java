package org.wallet.common.entity.admin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.wallet.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 菜单管理
 *
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "sys_menu")
public class SysMenuEntity extends BaseEntity {

    /**
     * 父菜单ID，一级菜单为0
     */
    @NotNull
    @Column(columnDefinition = "bigint(19) not null comment '父菜单ID'")
    private Long pid;
    /**
     * 菜单名称
     */
    @NotBlank
    @Column(columnDefinition = "varchar(100) not null comment '菜单名称'")
    private String name;
    /**
     * 菜单URL
     */
    @Column(columnDefinition = "varchar(200) not null comment '菜单URL'")
    private String url;
    /**
     * 授权标识(逗号分隔)
     */
    @Column(columnDefinition = "varchar(512) not null comment '授权标识'")
    private String permissions;
    /**
     * 类型(0：菜单|1：按钮)
     */
    @NotNull
    @Column(columnDefinition = "tinyint(1) not null comment '类型(0：菜单|1：按钮)'")
    private Integer type;
    /**
     * 菜单图标
     */
    @Column(columnDefinition = "varchar(50) comment '菜单图标'")
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