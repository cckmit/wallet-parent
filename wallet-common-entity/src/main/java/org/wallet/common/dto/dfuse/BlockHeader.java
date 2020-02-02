package org.wallet.common.dto.dfuse;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zengfucheng
 **/
@Data
public class BlockHeader {
    private String timestamp;
    private String producer;
    private Long confirmed;
    private String previous;
    @JsonProperty(value = "transaction_mroot")
    @JSONField(name = "transaction_mroot")
    private String transactionMRoot;
    @JsonProperty(value = "action_mroot")
    @JSONField(name = "action_mroot")
    private String actionMRoot;
    @JsonProperty(value = "schedule_version")
    @JSONField(name = "schedule_version")
    private Long scheduleVersion;
    @JsonProperty(value = "new_producers")
    @JSONField(name = "new_producers")
    private String newProducers;
    @JsonProperty(value = "header_extensions")
    @JSONField(name = "header_extensions")
    private List<String> headerExtensions;
}
