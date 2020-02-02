package org.wallet.common.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 实体基类
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JSONType(ignores = "$$_hibernate_interceptor")
public abstract class BaseEntity implements IBaseEntity{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(columnDefinition = "bigint(19) not null comment 'ID'")
	private Long id;
    /**
     * 创建者
     */
	@Column(columnDefinition = "bigint(19) not null comment '创建者'")
    private Long creator;
    /**
     * 创建时间
     */
    @CreatedDate
	@Column(columnDefinition = "datetime not null default current_timestamp comment '创建时间'")
    private Date createDate;

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!getClass().equals(obj.getClass())) {
			return false;
		}
		BaseEntity that = (BaseEntity) obj;
		return null != this.getId() && this.getId().equals(that.getId());
	}

	/**
	 * 是否覆盖更新
	 */
	@Transient
	@JSONField(serialize = false)
	private boolean coverageUpdate;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
