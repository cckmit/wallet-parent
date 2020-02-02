package org.wallet.common.entity.wallet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.wallet.common.entity.BaseEntity;
import org.wallet.common.enums.wallet.PaymentTypeEnum;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * EOS创建账号 支付配置
 * @author zengfucheng
 **/
@Getter
@Setter
@Entity
@Table(name = "wallet_pay_config", uniqueConstraints = @UniqueConstraint(name = "uk_wallet_pay_config", columnNames = "type"))
public class WalletPayConfigEntity extends BaseEntity {
    /**
     * 支付方式
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) not null comment '支付方式'")
    private PaymentTypeEnum type;
    /**
     * 名称
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(20) not null comment '名称'")
    private String name;
    /**
     * 支付金额
     */
    @NotNull
    @JSONField(format = "#0.0")
    @Column(columnDefinition = "decimal(22, 8) not null comment '支付金额'")
    private BigDecimal amount;
    /**
     * 金额单位
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(10) not null comment '金额单位'")
    private String unit;
    /**
     * 收款账号
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(64) not null comment '收款账号'")
    private String receiptAccount;
    /**
     * icon
     */
    @Column(columnDefinition = "varchar(256) default null comment 'icon'")
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
