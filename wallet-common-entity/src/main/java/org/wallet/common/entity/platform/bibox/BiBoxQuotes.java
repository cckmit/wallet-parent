package org.wallet.common.entity.platform.bibox;


import java.io.Serializable;

/**
 * 市场行情
 * @author zengfucheng
 **/
public class BiBoxQuotes implements Serializable {
    private static final long serialVersionUID = 5090120308857829958L;
    private int id;
    private String coin_symbol;     // 交易币种
    private String currency_symbol; // 定价币种
    private String last;            // 24h最新价
    private String high;            // 24h最高价
    private String low;             // 24h最低价
    private String change;          // 24h涨跌额度
    private String percent;         // 24h涨跌幅度
    private String vol24H;          // 24h成交量
    private String amount;          // 24h成交额
    private String last_cny;        // 最新人民币价格
    private String high_cny;        // 最高人民币价格
    private String low_cny;         // 最低人民币价格
    private String last_usd;        // 最新美元价格
    private String high_usd;        // 最高美元价格
    private String low_usd;         // 最低美元价格

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoin_symbol() {
        return coin_symbol;
    }

    public void setCoin_symbol(String coin_symbol) {
        this.coin_symbol = coin_symbol;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
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

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getVol24H() {
        return vol24H;
    }

    public void setVol24H(String vol24H) {
        this.vol24H = vol24H;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLast_cny() {
        return last_cny;
    }

    public void setLast_cny(String last_cny) {
        this.last_cny = last_cny;
    }

    public String getHigh_cny() {
        return high_cny;
    }

    public void setHigh_cny(String high_cny) {
        this.high_cny = high_cny;
    }

    public String getLow_cny() {
        return low_cny;
    }

    public void setLow_cny(String low_cny) {
        this.low_cny = low_cny;
    }

    public String getLast_usd() {
        return last_usd;
    }

    public void setLast_usd(String last_usd) {
        this.last_usd = last_usd;
    }

    public String getHigh_usd() {
        return high_usd;
    }

    public void setHigh_usd(String high_usd) {
        this.high_usd = high_usd;
    }

    public String getLow_usd() {
        return low_usd;
    }

    public void setLow_usd(String low_usd) {
        this.low_usd = low_usd;
    }
}
