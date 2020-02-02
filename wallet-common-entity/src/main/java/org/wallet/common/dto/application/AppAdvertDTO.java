package org.wallet.common.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;
import org.wallet.common.enums.application.AppAdvertPointEnum;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class AppAdvertDTO extends BaseNormalDTO {
    private Long chainId;
    private AppAdvertPointEnum point;
    private Long appId;
    private String appName;
    private String appIcon;
    private String intro;
    private String img;
    private String url;
    private String showTime;
}
