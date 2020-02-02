package org.wallet.common.dto.wallet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseDTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class WalletTransferLogDTO extends BaseDTO {
    @NotEmpty
    private String token;
    @NotEmpty
    private String contract;
    @NotEmpty
    private String coinName;
    @NotEmpty
    private String transferFrom;
    @NotEmpty
    private String transferTo;
    @NotNull
    @JSONField(format = "#0.0")
    private BigDecimal amount;
    @JSONField(format = "#0.0")
    private BigDecimal usdRate;
    @JSONField(format = "#0.0")
    private BigDecimal usdAmount;
}
