package org.wallet.common.dto;

import lombok.Data;

/**
 * @author zengfucheng
 **/
@Data
public class SimpleToken {
    /** 手机唯一标识 */
    private String clientId;
    /** Token */
    private String token;
    /** Token 超时时间（秒） */
    private String expire;
    /** 本次AES CBC 加密 IV */
    private String aesIv;
    /** 本次AES CBC 加密 Key */
    private String cryptoKey;
}
