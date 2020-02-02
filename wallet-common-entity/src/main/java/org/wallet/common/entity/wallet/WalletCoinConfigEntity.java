package org.wallet.common.entity.wallet;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.wallet.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 币种价格配置
 *
 * @author zengfucheng
 **/
@Getter
@Setter
@Entity
@Table(name = "wallet_coin_config")
public class WalletCoinConfigEntity extends BaseEntity {

    /**
     * 主链ID
     */
    @NotNull
    @Column(columnDefinition = "bigint(19) not null comment '主链ID'")
    private Long chainId;

    /**
     * 币种ID
     */
    @NotNull
    @Column(columnDefinition = "bigint(19) not null comment '币种ID'")
    private Long coinId;
    /**
     * 交易所ID
     */
    @NotNull
    @Column(columnDefinition = "bigint(19) not null comment '交易所ID'")
    private Long exchangeId;
    /**
     * 交易所币种主页
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(256) not null comment '交易所币种主页'")
    private String coinUrl;
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
