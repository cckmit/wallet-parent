package org.wallet.service.batch.service;

/**
 * @author zengfucheng
 **/
public interface PriceService {

    /**
     * 同步主链币种美元汇率
     */
    void syncChainCoinUSDPrice();

    /**
     * 同步所有币种行情价
     */
    void syncAllCoinPrice();
}
