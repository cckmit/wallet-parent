package org.wallet.common.dto.dfuse;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author zengfucheng
 **/
@Data
public class ExtDTrxOp {
    @JsonProperty(value = "src_trx_id")
    @JSONField(name = "src_trx_id")
    private String srcTrxId;
    @JsonProperty(value = "block_num")
    @JSONField(name = "block_num")
    private Long blockNum;
    @JsonProperty(value = "block_num")
    @JSONField(name = "block_num")
    private String blockId;
    @JsonProperty(value = "block_time")
    @JSONField(name = "block_time")
    private String blockTime;
}
