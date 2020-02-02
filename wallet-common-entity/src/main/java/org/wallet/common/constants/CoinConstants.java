package org.wallet.common.constants;

/**
 * @author zengfucheng
 **/
public interface CoinConstants {
    /** 法定货币 */
    String USD = "USD";
    String CNY = "CNY";

    /** 区块链币种 */
    String BTC = "BTC";
    String EOS = "EOS";
    String ETH = "ETH";
    String USDT = "USDT";

    /** 法币价格小数点位数*/
    Integer SCALE_PRICE = 2;
    /** 百分比小数点位数*/
    Integer SCALE_PERCENT = 4;
}
