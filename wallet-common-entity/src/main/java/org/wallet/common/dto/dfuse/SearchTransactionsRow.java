package org.wallet.common.dto.dfuse;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zengfucheng
 **/
@Data
public class SearchTransactionsRow {
    private TransactionLifecycle lifecycle;
    @JsonProperty(value = "action_idx")
    @JSONField(name = "action_idx")
    private List<Integer> actionIdx;
}
