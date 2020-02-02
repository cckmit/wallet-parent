package org.wallet.common.dto.admin;

import lombok.Data;

/**
 * @author zengfucheng
 **/
@Data
public class AdminToken {
    private String username;
    private String token;
    private String expire;
}
