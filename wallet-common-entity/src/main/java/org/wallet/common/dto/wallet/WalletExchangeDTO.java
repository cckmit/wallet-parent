package org.wallet.common.dto.wallet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;
import org.wallet.common.enums.wallet.QuotesSourceEnum;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class WalletExchangeDTO extends BaseNormalDTO {
    private QuotesSourceEnum quotesSource;
    private String icon;
    private String url;
}
