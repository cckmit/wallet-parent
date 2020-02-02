package org.wallet.common.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.wallet.common.dto.BaseNormalDTO;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AppChainNodeDTO extends BaseNormalDTO {
    private Long chainId;
    private String host;
    private Boolean def;
}
