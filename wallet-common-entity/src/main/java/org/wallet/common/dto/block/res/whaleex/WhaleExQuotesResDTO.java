package org.wallet.common.dto.block.res.whaleex;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class WhaleExQuotesResDTO extends WhaleExResultDTO {
    private String lastUpdateId;
    private Long timestamp;
    private Integer symbolId;
    private Integer baseCurrencyId;
    private Integer quoteCurrencyId;
    private BigDecimal priceChange;
    private BigDecimal priceChangePercent;
    private BigDecimal lastPrice;
    private BigDecimal lastQty;
    private BigDecimal quoteVolume;
    private BigDecimal baseVolume;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
}
