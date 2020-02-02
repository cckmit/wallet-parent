package org.wallet.dap.common.log;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.wallet.dap.common.dubbo.ResponseCode;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;

/**
 * @author zengfucheng
 * @date 2018年9月20日
 */
@Activate(group = Constants.PROVIDER)
public class DubboProviderLogFilter extends AbstractDubboLogFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		Object [] args = invocation.getArguments();
		if(null != args && args.length == 1 && args[0] instanceof ServiceRequest) {
			ServiceRequest request = (ServiceRequest)args[0];
			request.setReqTime(System.currentTimeMillis());
			ServiceResponse response = checkParameters(request);
			if(ResponseCode.ILLEGAL_PARAM.equals(response.getRespCode())) {
				return new RpcResult() {
					private static final long serialVersionUID = 1L;
					{setValue(LoggerService.print(request, response));}
				};
			}
			Result result = invoker.invoke(invocation);
			Throwable e = result.getException();
			if(e == null) {
				Object resultVal = result.getValue();
				LoggerService.print(request, (ServiceResponse) resultVal);
				return result;
			} else {
				return new RpcResult() {
					private static final long serialVersionUID = 2462670239388017211L;
					{setValue(LoggerService.print(request, ServiceResponse.newInstance(), result.getException()));}
				};
			}
		} 
		return invoker.invoke(invocation);
	}
}
