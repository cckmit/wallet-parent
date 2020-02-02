package org.wallet.common.dto.dfuse;

import lombok.Data;

/**
 * @author zengfucheng
 **/
@Data
public class AccountRAMDelta {
    private String account;
    private Long delta;
}
