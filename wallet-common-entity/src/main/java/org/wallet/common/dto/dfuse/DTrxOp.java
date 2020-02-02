package org.wallet.common.dto.dfuse;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author zengfucheng
 **/
@Data
public class DTrxOp {
    private String op;
    @JsonProperty(value = "action_idx")
    @JSONField(name = "action_idx")
    private Integer actionIdx;
    private String sender;
    @JsonProperty(value = "sender_id")
    @JSONField(name = "sender_id")
    private String senderId;
    private String payer;
    @JsonProperty(value = "published_at")
    @JSONField(name = "published_at")
    private String publishedAt;
    @JsonProperty(value = "delay_until")
    @JSONField(name = "delay_until")
    private String delayUntil;
    @JsonProperty(value = "expiration_at")
    @JSONField(name = "expiration_at")
    private String expirationAt;
    @JsonProperty(value = "trx_id")
    @JSONField(name = "trx_id")
    private String trxId;
    private String trx;
}
