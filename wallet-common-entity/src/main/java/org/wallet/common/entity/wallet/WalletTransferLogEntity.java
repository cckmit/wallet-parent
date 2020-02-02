package org.wallet.common.entity.wallet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.wallet.common.entity.BaseEntity;

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
@Table(name = "wallet_log_transfer", indexes = {
        @Index(name = "idx_w_l_t_token", columnList = "token"),
        @Index(name = "idx_w_l_t_contract", columnList = "contract"),
        @Index(name = "idx_w_l_t_coin_name", columnList = "coinName")})
public class WalletTransferLogEntity extends BaseEntity {
    /**
     * 主链币种
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(64) not null comment '主链币种'")
    private String token;
    /**
     * 合约
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(64) not null comment '合约'")
    private String contract;
    /**
     * 币种名称
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(64) not null comment '币种名称'")
    private String coinName;
    /**
     * 转账来源
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(64) not null comment '转账来源'")
    private String transferFrom;
    /**
     * 转账目标
     */
    @Column(columnDefinition = "varchar(64) not null comment '转账目标'")
    private String transferTo;
    /**
     * 转账金额
     */
    @JSONField(format = "#0.0")
    @Column(columnDefinition = "decimal(22,8) default null comment '转账金额'")
    private BigDecimal amount;
    /**
     * 兑美元汇率
     */
    @JSONField(format = "#0.0")
    @Column(columnDefinition = "decimal(22,8) default null comment '兑美元汇率'")
    private BigDecimal usdRate;
    /**
     * 转账美元
     */
    @JSONField(format = "#0.0")
    @Column(columnDefinition = "decimal(22,8) default null comment '转账美元'")
    private BigDecimal usdAmount;
}
