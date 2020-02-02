package org.wallet.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.enums.BusinessDomainEnum;
import org.wallet.common.enums.TableExtAttrTypeEnum;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class TableExtAttrDTO extends BaseNormalDTO {
    private Long chainId;
    private BusinessDomainEnum domain;
    private TableExtAttrTypeEnum type;
    private String label;
    private Boolean required;
    private String defaultValue;
    private String pendingValue;
}
