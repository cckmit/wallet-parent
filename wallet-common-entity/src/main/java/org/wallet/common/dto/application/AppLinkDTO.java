package org.wallet.common.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;
import org.wallet.common.enums.application.AppLinkTypeEnum;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class AppLinkDTO extends BaseNormalDTO {
    private AppLinkTypeEnum type;
    private String url;
    private Boolean deletable;
}
