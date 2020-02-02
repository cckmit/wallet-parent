package org.wallet.common.dto.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;

/**
 * 异常日志
 *
 * @author zengfucheng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysErrorLogDTO extends BaseNormalDTO {
	private String requestUri;
	private String requestMethod;
	private String requestParams;
	private String userAgent;
	private String ip;
	private String errorInfo;

}