package org.wallet.common.entity.wallet;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.wallet.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
@Table(name = "wallet_block_trans", uniqueConstraints = @UniqueConstraint(name = "uk_wallet_block_trans", columnNames = "trxId"))
public class WalletBlockTransEntity extends BaseEntity {
    /**
     * 唯一交易ID
     */
    @NotNull
    @Column(columnDefinition = "varchar(64) not null comment '唯一交易ID'")
    private String trxId;
    /**
     * 合约
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(20) not null comment '合约'")
    private String contract;
    /**
     * 发送账户
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(64) not null comment '发送账户'")
    private String sender;
    /**
     * 接收账户
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(64) not null comment '接收账户'")
    private String receiver;
    /**
     * 交易币种
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(12) not null comment '交易币种'")
    private String symbol;
    /**
     * 交易数量
     */
    @NotNull
    @Column(columnDefinition = "decimal(22,8) not null comment '交易数量'")
    private BigDecimal quantity;
    /**
     * 交易状态
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(32) not null comment '交易状态'")
    private String status;
    /**
     * 备注
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(4096) not null comment '备注'")
    private String memo;
    /**
     * 区块高度
     */
    @NotNull
    @Column(columnDefinition = "bigint(19) not null comment '区块高度'")
    private Long blockNum;
    /**
     * 交易时间
     */
    @NotNull
    @Column(columnDefinition = "datetime not null comment '交易时间'")
    private Date timestamp;
}
