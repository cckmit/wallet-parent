package org.wallet.common.entity.platform.bitz;

import java.io.Serializable;
import java.util.List;

/**
 * Bit-Z 行情数据
 * @author zengfucheng
 **/
public class BitZKLine implements Serializable {
    private static final long serialVersionUID = -5848707554833443674L;
    private String resolution;
    private String symbol;
    private String from;
    private String to;
    private int size;
    private List<BitZKLineData> bars;

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<BitZKLineData> getBars() {
        return bars;
    }

    public void setBars(List<BitZKLineData> bars) {
        this.bars = bars;
    }
}
