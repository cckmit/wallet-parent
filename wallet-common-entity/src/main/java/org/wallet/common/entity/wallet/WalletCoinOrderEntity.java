package org.wallet.common.entity.wallet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.wallet.common.entity.BaseEntity;
import org.wallet.common.enums.wallet.WalletCoinOrderStatusEnum;
import org.wallet.common.enums.wallet.PaymentTypeEnum;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 币种
 *
 * @author zengfucheng
 **/
@Getter
@Setter
@Entity
@Table(name = "wallet_coin_order", uniqueConstraints = @UniqueConstraint(name = "uk_wallet_coin_order", columnNames = {"no"}))
public class WalletCoinOrderEntity extends BaseEntity {
    /**
     * 订单号
     */
    @NotNull
    @Column(columnDefinition = "varchar(64) not null comment '订单号'")
    private String no;
    /**
     * 币种名称
     */
    @NotNull
    @Column(columnDefinition = "varchar(64) not null comment '币种名称'")
    private String coinName;
    /**
     * 支付方式
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) not null comment '支付方式'")
    private PaymentTypeEnum paymentType;
    /**
     * 邮箱
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(64) not null comment '邮箱'")
    private String email;
    /**
     * 手机号
     */
    @Column(columnDefinition = "varchar(20) default null comment '手机号'")
    private String phone;
    /**
     * 交易备注
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(64) not null comment '交易备注'")
    private String memo;
    /**
     * 订单金额
     */
    @NotNull
    @JSONField(format = "#0.0")
    @Column(columnDefinition = "decimal(22,8) not null comment '订单金额'")
    private BigDecimal amount;
    /**
     * 金额币种
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(12) not null comment '金额币种'")
    private String symbol;
    /**
     * 发送账户
     */
    @Column(columnDefinition = "varchar(64) not null comment '发送账户'")
    private String sender;
    /**
     * 接收账户
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(64) not null comment '接收账户'")
    private String receiver;
    /**
     * 交易状态
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) not null comment '交易状态'")
    private WalletCoinOrderStatusEnum status;
    /**
     * 唯一交易ID
     */
    @Column(columnDefinition = "varchar(64) not null comment '唯一交易ID'")
    private String trxId;
    /**
     * 邀请码
     */
    @Column(columnDefinition = "varchar(64) not null comment '邀请码'")
    private String inviteCode;
    /**
     * 更新时间
     */
    @LastModifiedDate
    @Column(columnDefinition = "datetime not null default current_timestamp comment '更新时间'")
    private Date updateDate;
}
