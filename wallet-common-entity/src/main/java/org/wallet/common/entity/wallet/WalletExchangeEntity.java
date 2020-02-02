package org.wallet.common.entity.wallet;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.wallet.common.entity.BaseEntity;
import org.wallet.common.enums.wallet.QuotesSourceEnum;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 交易所
 * @author zengfucheng
 **/
@Getter
@Setter
@Entity
@Table(name = "wallet_exchange", uniqueConstraints = @UniqueConstraint(name = "uk_wallet_exchange", columnNames = "name"))
public class WalletExchangeEntity extends BaseEntity {

    /**
     * 行情来源
     */
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(50) default null comment '行情来源'")
    private QuotesSourceEnum quotesSource;

    /**
     * 名称
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(50) not null comment '名称'")
    private String name;
    /**
     * 图标
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(256) not null comment '图标'")
    private String icon;
    /**
     * 主页
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(256) not null comment '主页'")
    private String url;
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
