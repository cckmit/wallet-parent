package org.wallet.common.dto.wallet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.wallet.common.enums.wallet.QuotesSourceEnum;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 币种价格配置
 * @author zengfucheng
 **/
@Data
public class ExchangePriceDTO implements Serializable {
    /** 币种配置ID */
    private Long configId;
    /** 交易所名称 */
    private String exchangeName;
    /** 行情来源 */
    private QuotesSourceEnum quotesSource;
    /** 该币种兑美元汇率 1 Coin: N USD */
    @JSONField(format = "#0.0")
    private BigDecimal priceUSDRate;
    /** 该币种兑人民币汇率 1 Coin: N CNY */
    @JSONField(format = "#0.0")
    private BigDecimal priceCNYRate;
    /** 配置排序 */
    private Integer sort;

    public ExchangePriceDTO() {
    }

    public ExchangePriceDTO(Long configId, String exchangeName, QuotesSourceEnum quotesSource, Integer sort) {
        this.configId = configId;
        this.exchangeName = exchangeName;
        this.quotesSource = quotesSource;
        this.sort = sort;
    }
}
