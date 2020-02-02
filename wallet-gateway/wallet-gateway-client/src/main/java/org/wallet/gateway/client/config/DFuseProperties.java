package org.wallet.gateway.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zengfucheng
 **/
@Data
@ConfigurationProperties(prefix = "dfuse")
public class DFuseProperties {
    /**
     * 是否清除DFuse查询索引指针
     */
    private Boolean clearCursor = false;

    /**
     * 合约
     */
    private String contract = "eosio.token";

    /**
     * 开始查询的区块高度
     */
    private Long startBlock;

    /**
     * DFuse Api Key
     */
    private String apiKey = "mobile_b663475c00420b874a93fa8566d7b406";

    /**
     * DFuse Api Host
     */
    private String host = "https://mainnet.eos.dfuse.io/";
}
