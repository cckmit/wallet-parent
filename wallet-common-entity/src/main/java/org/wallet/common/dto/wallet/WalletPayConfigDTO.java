package org.wallet.common.dto.wallet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;
import org.wallet.common.dto.SearchOperator;
import org.wallet.common.dto.SearchProperty;
import org.wallet.common.enums.wallet.PaymentTypeEnum;

import java.math.BigDecimal;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class WalletPayConfigDTO extends BaseNormalDTO {
    private PaymentTypeEnum type;
    @SearchProperty(SearchOperator.allLike)
    private String name;
    @JSONField(format = "#0.0")
    private BigDecimal amount;
    private String unit;
    private String receiptAccount;
    private String icon;

}
