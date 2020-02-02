package org.wallet.common.entity.platform.bitz;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Bit-Z 行情数据
 * @author zengfucheng
 **/
public class BitZQuotes implements Serializable {
    private static final long serialVersionUID = 4937767981521775455L;
    private String symbol;
    private BigDecimal quoteVolume;
    private BigDecimal volume;
    private String priceChange;
    private String priceChange24h;
    private BigDecimal askPrice;
    private BigDecimal askQty;
    private BigDecimal bidPrice;
    private BigDecimal bidQty;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal now;
    private Long firstId;
    private Long lastId;
    private Long dealCount;
    private Integer numberPrecision;
    private Integer pricePrecision;
    private BigDecimal cny;
    private BigDecimal usd;
    private BigDecimal krw;
    private BigDecimal jpy;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getQuoteVolume() {
        return quoteVolume;
    }

    public void setQuoteVolume(BigDecimal quoteVolume) {
        this.quoteVolume = quoteVolume;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(String priceChange) {
        this.priceChange = priceChange;
    }

    public String getPriceChange24h() {
        return priceChange24h;
    }

    public void setPriceChange24h(String priceChange24h) {
        this.priceChange24h = priceChange24h;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(BigDecimal askPrice) {
        this.askPrice = askPrice;
    }

    public BigDecimal getAskQty() {
        return askQty;
    }

    public void setAskQty(BigDecimal askQty) {
        this.askQty = askQty;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public BigDecimal getBidQty() {
        return bidQty;
    }

    public void setBidQty(BigDecimal bidQty) {
        this.bidQty = bidQty;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getNow() {
        return now;
    }

    public void setNow(BigDecimal now) {
        this.now = now;
    }

    public Long getFirstId() {
        return firstId;
    }

    public void setFirstId(Long firstId) {
        this.firstId = firstId;
    }

    public Long getLastId() {
        return lastId;
    }

    public void setLastId(Long lastId) {
        this.lastId = lastId;
    }

    public Long getDealCount() {
        return dealCount;
    }

    public void setDealCount(Long dealCount) {
        this.dealCount = dealCount;
    }

    public Integer getNumberPrecision() {
        return numberPrecision;
    }

    public void setNumberPrecision(Integer numberPrecision) {
        this.numberPrecision = numberPrecision;
    }

    public Integer getPricePrecision() {
        return pricePrecision;
    }

    public void setPricePrecision(Integer pricePrecision) {
        this.pricePrecision = pricePrecision;
    }

    public BigDecimal getCny() {
        return cny;
    }

    public void setCny(BigDecimal cny) {
        this.cny = cny;
    }

    public BigDecimal getUsd() {
        return usd;
    }

    public void setUsd(BigDecimal usd) {
        this.usd = usd;
    }

    public BigDecimal getKrw() {
        return krw;
    }

    public void setKrw(BigDecimal krw) {
        this.krw = krw;
    }

    public BigDecimal getJpy() {
        return jpy;
    }

    public void setJpy(BigDecimal jpy) {
        this.jpy = jpy;
    }
}
