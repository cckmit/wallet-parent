package org.wallet.common.constants.cache;

import org.wallet.common.constants.CoinConstants;

/**
 * 行情缓存
 *
 * @author zengfucheng
 **/
public interface QuotesCache extends CacheConstants {
    /** 行情缓存 */
    String CACHE_PREFIX = "Quotes";

    /** 行情交易对定价币种 */
    String ANCHOR = CoinConstants.USDT;

    /**
     * 交易行情<br/>
     * Key: Quotes:{Anchor|EOS}:{Contract|athenastoken}:{CoinName|ATHENA}:{QuotesSourceEnum}<br/>
     * Value: org.wallet.common.dto.wallet.QuotesDTO
     */
    String QUOTES = CACHE_PREFIX;

    /**
     * 币种兑法定货币平均汇率<br/>
     * Key: QuotesAvg:{Anchor|EOS}:{Contract|athenastoken}:{CoinName|ATHENA}<br/>
     * Value: org.wallet.common.dto.wallet.CoinAvgPriceDTO
     */
    String AVG = CACHE_PREFIX + "Avg";

    /** 币种兑法定货币汇率 */
    String PRICE = "Price";

    /**
     * 币种兑美元汇率<br/>
     * Key: Price:USD:{Anchor|EOS}<br/>
     * Key: Price:USD:{Anchor|EOS}:{Contract|athenastoken}:{CoinName|ATHENA}<br/>
     * Value: java.math.BigDecimal
     */
    String PRICE_USD = PRICE + SEP + CoinConstants.USD;

    /**
     * 币种兑人民币汇率
     * Key: Price:CNY:{CoinName}<br/>
     * Value: java.math.BigDecimal
     */
    String PRICE_CNY = PRICE + SEP + CoinConstants.CNY;
}
