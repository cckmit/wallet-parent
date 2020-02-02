package org.wallet.common.dto.dfuse;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zengfucheng
 **/
@Data
public class Action {
    private String account;
    private String name;
    private List<PermissionLevel> authorization;
    private Object data;
    @JsonProperty(value = "hex_data")
    @JSONField(name = "hex_data")
    private String hexData;
}
