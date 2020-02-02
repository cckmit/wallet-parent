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
 * 部门管理
 * 
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "sys_dept")
public class SysDeptEntity extends BaseEntity {

	/**
	 * 上级ID
	 */
	@NotNull
	@Column(columnDefinition = "bigint(19) not null comment '上级ID'")
	private Long pid;
	/**
	 * 所有上级ID，用逗号分开
	 */
	@NotNull
	@Column(columnDefinition = "varchar(1024) not null comment '上级ID'")
	private String pids;
	/**
	 * 部门名称
	 */
    @NotBlank
	@Column(columnDefinition = "varchar(50) not null comment '部门名称'")
	private String name;
	/**
	 * 排序
	 */
	@Column(columnDefinition = "int(4) not null default 0 comment '排序'")
	private Integer sort;
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