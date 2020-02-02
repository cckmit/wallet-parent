package org.wallet.common.dto.block.res.newdex;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zengfucheng
 **/
@Data
public class NewdexQuotesResDTO {
    private String symbol;
    private String contract;
    private String currency;
    private BigDecimal last;
    private BigDecimal change;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal amount;
    private BigDecimal volume;
}
