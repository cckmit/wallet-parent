package org.wallet.common.entity.wallet;

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
 * 钱包信息
 *
 * @author zengfucheng
 **/
@Getter
@Setter
@Entity
@Table(name = "wallet_info")
public class WalletInfoEntity extends BaseEntity {

    /**
     * 名称
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(50) not null comment '名称'")
    private String name;
    /**
     * 商标
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(256) not null comment 'logo'")
    private String logo;
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
