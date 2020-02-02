package org.wallet.common.dto.wallet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class WalletBlockTransDTO extends BaseNormalDTO {
    private String trxId;
    private String contract;
    private String sender;
    private String receiver;
    private String symbol;
    private BigDecimal quantity;
    private String status;
    private String memo;
    private Long blockNum;
    private Date timestamp;
}
