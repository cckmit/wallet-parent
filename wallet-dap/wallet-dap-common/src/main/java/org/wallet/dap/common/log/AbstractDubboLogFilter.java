package org.wallet.dap.common.log;

import org.wallet.common.constants.field.ServiceField;
import org.wallet.dap.common.dubbo.Responses;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;

/**
 * @author zengfucheng
 * @date 2018年9月20日
 */
public abstract class AbstractDubboLogFilter {

	protected ServiceResponse checkParameters(ServiceRequest request) {
		String serviceId = request.getServiceId();
		String methodName = request.getMethodName();
		String bizTrackNo = request.getBizTrackNo();
		ServiceResponse response = ServiceResponse.newInstance();
		if(serviceId == null || "".equals(serviceId.trim())) {
			return Responses.missingParam(ServiceField.SERVICE_ID);
		}
		if(methodName == null || "".equals(methodName.trim())) {
			return Responses.missingParam(ServiceField.METHOD_NAME);
		}
		if(bizTrackNo == null || "".equals(bizTrackNo.trim())) {
			return Responses.missingParam(ServiceField.BIZ_TRACK_NO);
		}
		return response;
	}
}
