package org.wallet.common.dto.dfuse;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zengfucheng
 **/
@Data
public class SearchTransResult {
    private String cursor;
    private List<SearchTransactionsRow> transactions;
    @JsonProperty(value = "forked_head_warning")
    @JSONField(name = "forked_head_warning")
    private String forkedHeadWarning;
    private String code;
    private String message;
    private String trace_id;
}
