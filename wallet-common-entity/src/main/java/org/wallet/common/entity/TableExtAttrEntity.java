package org.wallet.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.wallet.common.enums.BusinessDomainEnum;
import org.wallet.common.enums.TableExtAttrTypeEnum;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author zengfucheng
 **/
@Getter
@Setter
@Entity
@Table(name = "table_ext_attr", uniqueConstraints = @UniqueConstraint(name = "uk_table_ext_attr", columnNames = {"chainId", "domain", "name"}))
public class TableExtAttrEntity extends BaseEntity {
    /**
     * 主链ID
     */
    @NotNull
    @Column(columnDefinition = "bigint(19) not null comment '主链ID'")
    private Long chainId;
    /**
     * 所属业务领域
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(50) not null comment '所属业务领域'")
    private BusinessDomainEnum domain;

    /**
     * 字段类型
     */
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) not null comment '字段类型'")
    private TableExtAttrTypeEnum type;
    /**
     * 字段名称
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(50) not null comment '字段名称'")
    private String name;
    /**
     * 字段标签
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(50) not null comment '字段标签'")
    private String label;
    /**
     * 是否必填
     */
    @Column(columnDefinition = "tinyint(1) not null default 0 comment '是否必填'")
    private Boolean required;
    /**
     * 默认值
     */
    @Column(columnDefinition = "varchar(255) null comment '默认值'")
    private String defaultValue;
    /**
     * 备选值
     */
    @Column(columnDefinition = "varchar(255) null comment '备选值[,]逗号分隔'")
    private String pendingValue;
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
