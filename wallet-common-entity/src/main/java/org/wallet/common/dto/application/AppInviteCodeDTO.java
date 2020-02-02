package org.wallet.common.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;
import org.wallet.common.enums.application.AppInviteCodeStatusEnum;
import org.wallet.common.enums.application.AppInviteCodeTypeEnum;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class AppInviteCodeDTO extends BaseNormalDTO {
    private AppInviteCodeTypeEnum type;
    private String code;
    private AppInviteCodeStatusEnum status;
    private Long refId;
}
