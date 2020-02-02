package org.wallet.service.common.service.dubbo;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.Responses;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.service.common.ResponseCode;

import java.lang.reflect.Method;
import java.sql.SQLException;

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

        Object result = null;

        try{
            result = ReflectionUtils.invokeMethod(method, this, request, response);
        } catch (Exception e){
            error(methodName, request, response, e);
        }

        if(result instanceof ServiceResponse){
            return (ServiceResponse) result;
        } else{
            response.setResult(result);
            return response;
        }
    }

    public void error(String methodName, ServiceRequest request, ServiceResponse response, Throwable t) {
        Throwable cause = t.getCause();
        String msg = t.getMessage();
        if(cause instanceof ConstraintViolationException){
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) cause;
            String constraintName = constraintViolationException.getConstraintName();
            SQLException sqlException = constraintViolationException.getSQLException();
            if(!StringUtils.isEmpty(constraintName)){
                msg = String.format("违反约束[%s]：%s", constraintName, sqlException.getMessage());
            }
        }else{
            log.error("方法[{}]发生异常：{}", methodName, msg, t);
        }

        response.setRespCode(ResponseCode.SYS_ERROR);
        response.setRespMsg(msg);
    }
}
