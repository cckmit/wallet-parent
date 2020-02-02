package org.wallet.common.dto.wallet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;
import org.wallet.common.enums.wallet.PaymentTypeEnum;
import org.wallet.common.enums.wallet.WalletCoinOrderStatusEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class WalletCoinOrderDTO extends BaseNormalDTO {
    private String no;
    private String coinName;
    @NotNull
    private PaymentTypeEnum paymentType;
    @NotEmpty
    private String email;
    private String phone;
    private String memo;
    @JSONField(format = "#0.0")
    private BigDecimal amount;
    private String symbol;
    private String sender;
    private String receiver;
    private WalletCoinOrderStatusEnum status;
    private String trxId;
    private String inviteCode;
    private Date updateDate;
}
