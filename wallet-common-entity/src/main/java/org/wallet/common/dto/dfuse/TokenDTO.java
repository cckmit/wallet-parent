package org.wallet.common.dto.dfuse;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @author zengfucheng
 **/
@Data
public class TokenDTO {
    private String code;
    private String message;
    private String token;
    @JsonProperty(value = "expires_at")
    @JSONField(name = "expires_at")
    private Long expire;

    public boolean success(){
        return null != expire && !StringUtils.isEmpty(token);
    }

    public boolean error(){
        return !StringUtils.isEmpty(code) && !StringUtils.isEmpty(message);
    }
}
