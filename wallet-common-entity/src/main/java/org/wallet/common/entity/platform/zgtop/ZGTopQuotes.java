package org.wallet.common.entity.platform.zgtop;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ZG.TOP 行情数据
 * @author zengfucheng
 **/
public class ZGTopQuotes implements Serializable {
    private static final long serialVersionUID = 8566546130086538206L;
    private String symbol;
    private BigDecimal vol;
    private BigDecimal sell;
    private BigDecimal buy;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal last;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getVol() {
        return vol;
    }

    public void setVol(BigDecimal vol) {
        this.vol = vol;
    }

    public BigDecimal getSell() {
        return sell;
    }

    public void setSell(BigDecimal sell) {
        this.sell = sell;
    }

    public BigDecimal getBuy() {
        return buy;
    }

    public void setBuy(BigDecimal buy) {
        this.buy = buy;
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

    public BigDecimal getLast() {
        return last;
    }

    public void setLast(BigDecimal last) {
        this.last = last;
    }
}
