package org.wallet.common.dto;

import lombok.Data;

/**
 * 简单用户（缓存）
 * @author zengfucheng
 **/
@Data
public class SimpleUser {
    /**
     * ID
     */
    private Long id;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 账户名
     */
    private String account;
    /**
     * 手机号
     */
    private String phone;
}
