package org.wallet.common.dto.block.res.findex;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zengfucheng
 **/
@Data
public class FindexQuotesResDTO {
    private BigDecimal price;
    private BigDecimal change;
    private Integer pairId;
}
