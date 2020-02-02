package org.wallet.common.dto.wallet;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 24小时内实时行情
 *
 * @author zengfucheng
 **/
@Data
public class QuotesDTO implements Serializable {
    /** 交易币种 */
    private String symbol;
    /** 定价币种 */
    private String anchor;
    /** 24h最新价 */
    private BigDecimal last;
    /** 24h最高价 */
    private BigDecimal high;
    /** 24h最低价 */
    private BigDecimal low;
    /** 24h涨跌额度 */
    private BigDecimal change;
    /** 24h涨跌幅度 */
    private BigDecimal percent;
    /** 24h成交量 */
    private BigDecimal volume;
    /** 24h成交额 */
    private BigDecimal amount;
    /** 最新人民币价格 */
    private BigDecimal lastCNY;
    /** 最高人民币价格 */
    private BigDecimal highCNY;
    /** 最低人民币价格 */
    private BigDecimal lowCNY;
    /** 最新美元价格 */
    private BigDecimal lastUSD;
    /** 最高美元价格 */
    private BigDecimal highUSD;
    /** 最低美元价格 */
    private BigDecimal lowUSD;
    /** 最后更新时间 */
    private Date updated;
}
