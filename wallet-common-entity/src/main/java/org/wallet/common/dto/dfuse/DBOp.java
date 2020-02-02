package org.wallet.common.dto.dfuse;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author zengfucheng
 **/
@Data
public class DBOp {
    private String op;
    @JsonProperty(value = "action_idx")
    @JSONField(name = "action_idx")
    private Integer actionIdx;
    private String account;
    private String table;
    private String scope;
    private String key;
    private DBRow old;
    @JsonProperty(value = "new")
    @JSONField(name = "new")
    private DBRow newRow;
}
