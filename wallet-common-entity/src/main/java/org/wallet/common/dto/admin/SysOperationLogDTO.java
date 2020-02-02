package org.wallet.common.dto.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;
import org.wallet.common.dto.SearchOperator;
import org.wallet.common.dto.SearchProperty;

/**
 * 操作日志
 *
 * @author zengfucheng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOperationLogDTO extends BaseNormalDTO {
	private String operation;
	private String requestUri;
	private String requestMethod;
	private String requestParams;
	private Integer requestTime;
	private String responseBody;
	private String userAgent;
	private String ip;
	private Integer status;
	@SearchProperty(SearchOperator.allLike)
	private String creatorName;

}