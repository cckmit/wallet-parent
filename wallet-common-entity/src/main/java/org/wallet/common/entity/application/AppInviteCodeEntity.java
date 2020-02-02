package org.wallet.common.entity.application;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.wallet.common.entity.BaseEntity;
import org.wallet.common.enums.application.AppInviteCodeStatusEnum;
import org.wallet.common.enums.application.AppInviteCodeTypeEnum;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * App 邀请码
 * 
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "app_invite_code", uniqueConstraints = {
    @UniqueConstraint(name = "uk_app_invite_code", columnNames = {"type", "code"})
})
public class AppInviteCodeEntity extends BaseEntity {

    /**
     * 邀请码类型
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(50) not null comment '邀请码类型'")
    private AppInviteCodeTypeEnum type;
    /**
     * 邀请码
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(20) not null comment '邀请码'")
    private String code;
    /**
     * 邀请码状态
     */
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) not null comment '邀请码状态'")
    private AppInviteCodeStatusEnum status;
    /**
     * 关联业务ID
     */
    @Column(columnDefinition = "bigint(19) not null comment '关联业务ID'")
    private Long refId;
    /**
     * 更新时间
     */
    @LastModifiedDate
    @Column(columnDefinition = "datetime not null default current_timestamp comment '更新时间'")
    private Date updateDate;

}