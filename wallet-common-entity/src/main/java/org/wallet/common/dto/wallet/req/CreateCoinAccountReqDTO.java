package org.wallet.common.dto.wallet.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;

/**
 * 创建币种账户请求数据实体
 * @author zengfucheng
 **/
@Data
public class CreateCoinAccountReqDTO implements Serializable {
    /** 币种名称 */
    @NotBlank
    private String coinName;
    /** 账户名称 */
    @NotBlank
    @Pattern(regexp = "(^[1-5]{12}$)|(^[a-z]{12}$)|(^[1-5a-z]{12}$)")
    private String accountName;
    /** 账户公钥 */
    private String publicKey;
    /** 账户私钥 */
    private String privateKey;
    /** 邀请码 */
    @NotBlank
    private String inviteCode;
    private Map<String, String> data;
}
