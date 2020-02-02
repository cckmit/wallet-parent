package org.wallet.common.dto.block.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.block.BlackReqDTO;

import java.util.Map;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateAccountReqDTO extends BlackReqDTO {
    private String name;
    private String publicKey;
    private String privateKey;
    private Map<String, String> data;
}
