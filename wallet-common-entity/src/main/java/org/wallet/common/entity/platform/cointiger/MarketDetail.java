package org.wallet.common.entity.platform.cointiger;

import java.io.Serializable;

/**
 * CoinTiger 24 小时内市场行情
 * @author zengfucheng
 **/
public class MarketDetail implements Serializable {
    private static final long serialVersionUID = 3314232003890992514L;
    /**
     * 交易量
     */
    private String baseVolume;
    /**
     * 24小时内最高价格
     */
    private String high24hr;
    /**
     * 买一价格
     */
    private String highestBid;
    /**
     * 唯一标识
     */
    private String id;
    /**
     * 最新价
     */
    private String last;
    /**
     * 24小时内最低价
     */
    private String low24hr;
    /**
     * 卖一价格
     */
    private String lowestAsk;
    /**
     * 涨跌幅
     */
    private String percentChange;
    /**
     * 交易额
     */
    private String quoteVolume;

    public String getBaseVolume() {
        return baseVolume;
    }

    public void setBaseVolume(String baseVolume) {
        this.baseVolume = baseVolume;
    }

    public String getHigh24hr() {
        return high24hr;
    }

    public void setHigh24hr(String high24hr) {
        this.high24hr = high24hr;
    }

    public String getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(String highestBid) {
        this.highestBid = highestBid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getLow24hr() {
        return low24hr;
    }

    public void setLow24hr(String low24hr) {
        this.low24hr = low24hr;
    }

    public String getLowestAsk() {
        return lowestAsk;
    }

    public void setLowestAsk(String lowestAsk) {
        this.lowestAsk = lowestAsk;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(String percentChange) {
        this.percentChange = percentChange;
    }

    public String getQuoteVolume() {
        return quoteVolume;
    }

    public void setQuoteVolume(String quoteVolume) {
        this.quoteVolume = quoteVolume;
    }
}
