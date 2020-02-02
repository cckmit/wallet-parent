package org.wallet.common.entity.application;

import lombok.Getter;
import lombok.Setter;
import org.wallet.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * App 消息
 * 
 * @author zengfucheng
 */
@Getter
@Setter
@Entity
@Table(name = "app_message")
public class AppMessageEntity extends BaseEntity {

    /**
     * 标题
     */
    @NotEmpty
    @Column(columnDefinition = "varchar(50) not null comment '标题'")
    private String title;

    /**
     * 内容
     */
    @NotEmpty
    @Column(columnDefinition = "text not null comment '内容'")
    private String content;
    /**
     * 地址
     */
    @Column(columnDefinition = "varchar(256) not null comment '地址'")
    private String url;

}