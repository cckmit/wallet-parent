package org.wallet.common.entity.platform.cointiger;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * CoinTiger K线数据
 * @author zengfucheng
 **/
public class KLineObject implements Serializable {
    private static final long serialVersionUID = -3220454164115904573L;
    private String symbol;
    @JSONField(name = "kline_data")
    private List<KLineDetail> kLineDetails;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<KLineDetail> getkLineDetails() {
        return kLineDetails;
    }

    public void setkLineDetails(List<KLineDetail> kLineDetails) {
        this.kLineDetails = kLineDetails;
    }
}

