package org.wallet.common.entity.admin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.wallet.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 数据字典
 *
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "sys_dict")
public class SysDictEntity extends BaseEntity {

    /**
     * 字典类型
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(50) not null comment '字典类型'")
    private String dictType;
    /**
     * 字典名称
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(50) not null comment '字典名称'")
    private String dictName;
    /**
     * 字典值
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(50) not null comment '字典值'")
    private String dictValue;
    /**
     * 字典标签
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(50) not null comment '字典标签'")
    private String dictLabel;
    /**
     * 备注
     */
    @Column(columnDefinition = "varchar(255) comment '备注'")
    private String remark;
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