package org.wallet.common.dto.dfuse;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author zengfucheng
 **/
@Data
public class TableOp {
    private String op;
    @JsonProperty(value = "action_idx")
    @JSONField(name = "action_idx")
    private Integer actionIdx;
    private String payer;
    private String path;
}
