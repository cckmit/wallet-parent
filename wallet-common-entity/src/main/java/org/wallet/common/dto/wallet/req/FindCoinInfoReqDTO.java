package org.wallet.common.dto.wallet.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 查询币种信息请求数据实体
 * @author zengfucheng
 **/
@Data
public class FindCoinInfoReqDTO implements Serializable {
    /** 主链ID */
    private Long chainId;
    /** 币种名称 */
    private String coinName;
    /** 合约账户 */
    private String contract;
    /** 币种名称 */
    private List<String> coinNames;
    /** 币种名称(,分隔) */
    private String coinNameString;
}
