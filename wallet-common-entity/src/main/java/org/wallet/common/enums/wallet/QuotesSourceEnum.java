package org.wallet.common.enums.wallet;

import lombok.Getter;
import org.wallet.common.constants.QuotesConstants;

/**
 * 币种行情价格来源
 */
@Getter
public enum QuotesSourceEnum {
    /** Newdex 交易所 */
    Newdex("Newdex 交易所", "https://api.newdex.io", QuotesConstants.NEWDEX),
    /** FINDEX 交易所 */
    FINDEX("FINDEX 交易所", "https://api.findex.pro", QuotesConstants.FINDEX),
    /** WhaleEx 鲸交所 */
    WhaleEx("WhaleEx 鲸交所", "https://api.whaleex.com", QuotesConstants.WHALE_EX);

    /** 交易所标签 */
    private String label;
    /** 交易所API Host */
    private String host;
    /** Gateway-Client 服务中的QuotesService实例名称，用于获取Spring Bean */
    private String serviceName;

    QuotesSourceEnum(String label, String host, String serviceName) {
        this.label = label;
        this.host = host;
        this.serviceName = serviceName;
    }

    public String getName(){
        return this.name();
    }

}
