package org.wallet.gateway.client.service;

import java.math.BigDecimal;

/**
 * @author zengfucheng
 **/
public interface PriceService {
    /**
     * 获取主链币种美元汇率
     * @param coinName 币种名称
     * @return 美元汇率
     */
    BigDecimal getCoinUSDPrice(String coinName);
}
