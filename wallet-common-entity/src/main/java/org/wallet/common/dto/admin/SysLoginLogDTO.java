package org.wallet.common.dto.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;
import org.wallet.common.dto.SearchOperator;
import org.wallet.common.dto.SearchProperty;

/**
 * 登录日志
 *
 * @author zengfucheng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysLoginLogDTO extends BaseNormalDTO {
	private Integer operation;
	private Integer status;
	private String userAgent;
	private String ip;
    @SearchProperty(SearchOperator.allLike)
	private String creatorName;
}
