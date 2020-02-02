package org.wallet.common.dto.wallet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 币种价格实体
 * @author zengfucheng
 **/
@Data
public class WalletCoinPriceDTO implements Serializable {
    /** 主链币种ID */
    private Long chainCoinId;
    /** 主链币种名称 */
    private String chainCoinName;
    /** 币种ID */
    private Long coinId;
    /** 币种名称 */
    private String coinName;
    /** 币种合约 */
    private String contract;
    /** 兑美元平均汇率 */
    @JSONField(format = "#0.0")
    private BigDecimal avgUSDRate;
    /** 兑人民币平均汇率 */
    @JSONField(format = "#0.0")
    private BigDecimal avgCNYRate;
    /** 交易所行情价 */
    private List<ExchangePriceDTO> exchangeList;
    /** 排序第一的交易所名称 */
    private String topExchangeName;

    public WalletCoinPriceDTO() {
    }

    public WalletCoinPriceDTO(Long chainCoinId, String chainCoinName, Long coinId, String contract, String coinName) {
        this.chainCoinId = chainCoinId;
        this.chainCoinName = chainCoinName;
        this.coinId = coinId;
        this.contract = contract;
        this.coinName = coinName;
    }
}
