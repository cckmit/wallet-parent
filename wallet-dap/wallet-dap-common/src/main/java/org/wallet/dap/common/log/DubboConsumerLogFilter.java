package org.wallet.dap.common.log;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.wallet.dap.common.dubbo.ResponseCode;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;

/**
 * Dubbo 服务调用日志Filter
 * @author zengfucheng
 **/
@Activate(group = Constants.CONSUMER)
public class DubboConsumerLogFilter extends AbstractDubboLogFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Object [] args = invocation.getArguments();
        if(null != args && args.length == 1 && args[0] instanceof ServiceRequest){
        	ServiceRequest request = (ServiceRequest)args[0];
			final ServiceResponse response = checkParameters(request);
			if(ResponseCode.ILLEGAL_PARAM.equals(response.getRespCode())) {
				return new RpcResult() {
					private static final long serialVersionUID = 1L;
					{setValue(LoggerService.print(request, response));}
				};
			} 
			Result result = invoker.invoke(invocation);
			Object resultVal = result.getValue();
			if(resultVal instanceof ServiceResponse) {
				LoggerService.print(request, (ServiceResponse) resultVal);
				return result;
			};
        }
        return invoker.invoke(invocation);
    }
}
