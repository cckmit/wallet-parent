package org.wallet.common.dto.dfuse;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zengfucheng
 **/
@Data
public class Transaction {
    private String expiration;
    @JsonProperty(value = "ref_block_num")
    @JSONField(name = "ref_block_num")
    private Long refBlockNum;
    @JsonProperty(value = "ref_block_prefix")
    @JSONField(name = "ref_block_prefix")
    private Long refBlockPrefix;
    @JsonProperty(value = "max_net_usage_words")
    @JSONField(name = "max_net_usage_words")
    private Long maxNetUsageWords;
    @JsonProperty(value = "max_cpu_usage_ms")
    @JSONField(name = "max_cpu_usage_ms")
    private Long maxCpuUsageMs;
    @JsonProperty(value = "delay_sec")
    @JSONField(name = "delay_sec")
    private Long delaySec;
    @JsonProperty(value = "context_free_actions")
    @JSONField(name = "context_free_actions")
    private List<Action> contextFreeActions;
    private List<Action> actions;
    @JsonProperty(value = "transaction_extensions")
    @JSONField(name = "transaction_extensions")
    private List<String> transactionExtensions;
    private List<String> signatures;
    @JsonProperty(value = "context_free_data")
    @JSONField(name = "context_free_data")
    private List<String> contextFreeData;
}
