package org.wallet.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.wallet.common.enums.BusinessDomainEnum;
import org.wallet.common.enums.TableExtAttrTypeEnum;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author zengfucheng
 **/
@Getter
@Setter
@Entity
@Table(name = "table_ext_attr_value")
public class TableExtAttrValueEntity extends BaseEntity {
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
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) not null comment '字段类型'")
    private TableExtAttrTypeEnum type;
    /**
     * 属性ID
     */
    @NotNull
    @Column(columnDefinition = "bigint(19) not null comment '属性ID'")
    private Long attrId;
    /**
     * 数据ID
     */
    @NotNull
    @Column(columnDefinition = "bigint(19) not null comment '数据ID'")
    private Long dataId;
    /**
     * 字段标签
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(50) not null comment '字段标签'")
    private String label;
    /**
     * 字段名称
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(50) not null comment '字段名称'")
    private String name;
    /**
     * 字段值
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(255) not null comment '字段值'")
    private String value;
    /**
     * 排序
     */
    @Column(columnDefinition = "int(4) not null default 0 comment '排序'")
    private Integer sort;
}
