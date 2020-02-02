package org.wallet.common.dto.dfuse;

import lombok.Data;

/**
 * @author zengfucheng
 **/
@Data
public class DBRow {
    private String payer;
    private String hex;
    private String json;
    private String error;
}
