package org.wallet.common.dto.block.req;

import lombok.Data;
import org.wallet.common.enums.wallet.QuotesSourceEnum;

import java.io.Serializable;

/**
 * @author zengfucheng
 **/
@Data
public class QuotesReqDTO implements Serializable {
    private QuotesSourceEnum source;
    private String contract;
    private String symbol;
    private String anchor;
}
