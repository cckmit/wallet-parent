package org.wallet.common.entity.application;

import lombok.Getter;
import lombok.Setter;
import org.wallet.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * App 访问日志
 * 
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "app_log_visit", indexes = {
        @Index(name = "idx_a_l_v_chain_id", columnList = "chainId"),
        @Index(name = "idx_a_l_v_app_id", columnList = "appId")
})
public class AppVisitLogEntity extends BaseEntity {
    /**
     * 主链ID
     */
    @Column(columnDefinition = "bigint(19) not null comment '主链ID'")
    private Long chainId;
    /**
     * App ID
     */
    @Column(columnDefinition = "bigint(19) not null comment 'App ID'")
    private Long appId;
    /**
     * 应用名称
     */
    @Column(columnDefinition = "varchar(64) not null comment '应用名称'")
    private String appName;
    /**
     * 主链币种名称
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(64) not null comment '主链币种名称'")
    private String token;
    /**
     * contract
     */
    @Column(columnDefinition = "varchar(64) comment '合约'")
    private String contract;
    /**
     * 展示时间
     */
    @Column(columnDefinition = "varchar(64) comment '币种名称'")
    private String coinName;
	/**
	 * 账号名称
	 */
    @Column(columnDefinition = "varchar(64) not null comment '账号名称'")
	private String account;

}