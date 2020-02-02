package org.wallet.common.dto.dfuse;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author zengfucheng
 **/
@Data
public class TransactionReceiptHeader {
    private String status;
    @JsonProperty(value = "cpu_usage_us")
    @JSONField(name = "cpu_usage_us")
    private Long cpuUsageUs;
    @JsonProperty(value = "net_usage_words")
    @JSONField(name = "net_usage_words")
    private Long netUsageWords;
}
