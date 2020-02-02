package org.wallet.common.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class AppVisitLogDTO extends BaseNormalDTO {
    private Long chainId;
    private Long appId;
    private String appName;
    private String token;
    private String contract;
    private String coinName;
    private String account;
}
