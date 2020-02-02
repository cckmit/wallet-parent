package org.wallet.common.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;
import org.wallet.common.enums.application.AppTagEnum;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class AppTagDTO extends BaseNormalDTO {
    private Long chainId;
    private Long typeId;
    private Long appId;
    private AppTagEnum tag;
    private String img;
}
