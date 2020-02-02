package org.wallet.common.dto.wallet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class WalletInfoDTO extends BaseNormalDTO {
    private String logo;
}
