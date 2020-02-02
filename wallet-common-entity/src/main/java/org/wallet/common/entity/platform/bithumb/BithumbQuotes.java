package org.wallet.common.entity.platform.bithumb;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Bithumb 行情数据
 * @author zengfucheng
 **/
public class BithumbQuotes implements Serializable {
    private static final long serialVersionUID = 8566546130086538206L;
    /**
     * symbol
     */
    private String s;
    /**
     * deal quantity in the past of hours
     */
    private BigDecimal v;
    /**
     * price change in the past of hours
     */
    private BigDecimal p;
    /**
     * high price in the past of 24 hours
     */
    private BigDecimal h;
    /**
     * low price in the past of 24 hours
     */
    private BigDecimal l;
    /**
     * last price in the past of 24 hours
     */
    private BigDecimal c;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public BigDecimal getV() {
        return v;
    }

    public void setV(BigDecimal v) {
        this.v = v;
    }

    public BigDecimal getP() {
        return p;
    }

    public void setP(BigDecimal p) {
        this.p = p;
    }

    public BigDecimal getH() {
        return h;
    }

    public void setH(BigDecimal h) {
        this.h = h;
    }

    public BigDecimal getL() {
        return l;
    }

    public void setL(BigDecimal l) {
        this.l = l;
    }

    public BigDecimal getC() {
        return c;
    }

    public void setC(BigDecimal c) {
        this.c = c;
    }
}
