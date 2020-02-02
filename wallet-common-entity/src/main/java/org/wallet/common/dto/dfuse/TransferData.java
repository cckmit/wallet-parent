package org.wallet.common.dto.dfuse;

import lombok.Data;

/**
 * @author zengfucheng
 **/
@Data
public class TransferData {
    private String from;
    private String to;
    private String quantity;
    private String memo;
}
