package org.wallet.common.dto.wallet;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 币种行情均价
 * @author zengfucheng
 **/
@Data
public class CoinAvgPriceDTO implements Serializable {
    private String coinName;
    private BigDecimal avgCNYRate;
    private BigDecimal avgUSDRate;
}
