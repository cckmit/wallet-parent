package org.wallet.common.entity.platform.bibox;

import java.io.Serializable;

/**
 * K线行情
 * @author zengfucheng
 **/
public class KLineData implements Serializable {
    private static final long serialVersionUID = -4912508928984850493L;
    private Long time;      // 交易币种
    private String open;    // 24h最新价
    private String high;    // 24h最高价
    private String low;     // 24h最低价
    private String close;   // 24h涨跌额度
    private String vol;     // 24h涨跌幅度

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }
}
