package org.wallet.gateway.client.service.dubbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.Responses;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;

import java.lang.reflect.Method;

/**
 * @author zengfucheng
 **/
public abstract class BaseDubboService implements IService {
    protected Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public ServiceResponse invoke(ServiceRequest request) {
        String methodName = request.getMethodName();

        Class dubboServiceClass = this.getClass();

        Method method = ReflectionUtils.findMethod(this.getClass(), methodName, ServiceRequest.class, ServiceResponse.class);

        if(null == method){
            return Responses.notFound(methodName);
        }

        ServiceResponse response = ServiceResponse.newInstance();

        Object result = ReflectionUtils.invokeMethod(method, this, request, response);

        if(result instanceof ServiceResponse){
            return (ServiceResponse) result;
        } else{
            response.setResult(result);
            return response;
        }
    }
}
