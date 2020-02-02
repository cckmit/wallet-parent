package org.wallet.common.dto.dfuse;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zengfucheng
 **/
@Data
public class ActionTrace {
    private ActionReceipt receipt;
    private Action act;
    @JsonProperty(value = "context_free")
    @JSONField(name = "context_free")
    private Boolean contextFree;
    private Long elapsed;
    private String console;
    @JsonProperty(value = "trx_id")
    @JSONField(name = "trx_id")
    private String trxId;
    @JsonProperty(value = "block_num")
    @JSONField(name = "block_num")
    private Long blockNum;
    @JsonProperty(value = "block_time")
    @JSONField(name = "block_time")
    private String blockTime;
    @JsonProperty(value = "producer_block_id")
    @JSONField(name = "producer_block_id")
    private String producerBlockId;
    @JsonProperty(value = "account_ram_deltas")
    @JSONField(name = "account_ram_deltas")
    private List<AccountRAMDelta> accountRamDeltas;
    private String except;
    @JsonProperty(value = "inline_traces")
    @JSONField(name = "inline_traces")
    private List<ActionTrace> inlineTraces;
}
