package org.wallet.common.entity.platform.bitz;

import java.io.Serializable;

/**
 * @author zengfucheng
 **/
public class BitZKLineData implements Serializable {
    private static final long serialVersionUID = 2437326475268409394L;
    private String time;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;
    private String datetime;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
