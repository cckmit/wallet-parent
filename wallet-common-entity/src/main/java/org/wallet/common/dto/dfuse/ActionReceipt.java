package org.wallet.common.dto.dfuse;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zengfucheng
 **/
@Data
public class ActionReceipt {
    private String receiver;
    @JsonProperty(value = "act_digest")
    @JSONField(name = "act_digest")
    private String actDigest;
    @JsonProperty(value = "global_sequence")
    @JSONField(name = "global_sequence")
    private String globalSequence;
    @JsonProperty(value = "auth_sequence")
    @JSONField(name = "auth_sequence")
    private List<String> authSequence;
    @JsonProperty(value = "recv_sequence")
    @JSONField(name = "recv_sequence")
    private Long recvSequence;
    @JsonProperty(value = "code_sequence")
    @JSONField(name = "code_sequence")
    private Long codeSequence;
    @JsonProperty(value = "abi_sequence")
    @JSONField(name = "abi_sequence")
    private Long abiSequence;
}
