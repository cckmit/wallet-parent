package org.wallet.web.admin.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wallet.web.admin.annotation.OperationLog;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zengfucheng
 **/
@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private LogService logService;

    @Autowired
    private HttpServletRequest request;

    @Around(value = "@annotation(org.wallet.web.admin.annotation.OperationLog)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        OperationLog annotation = method.getAnnotation(OperationLog.class);
        Object[] args = point.getArgs();
        Object param = null;
        if(null != args){
            if(args.length > 1){
                List<Serializable> argList = new ArrayList<>();
                for(Object arg : args){
                    if(arg instanceof Serializable){
                        argList.add((Serializable) arg);
                    }
                }
                param = argList;
            }else if(args.length == 1){
                param = args[0];
            }
        }
        long beginTime = System.currentTimeMillis();
        Object result;
        try {
            result = point.proceed();
            long time = System.currentTimeMillis() - beginTime;
            logService.operationLog(request, annotation.value(), time, param, result, null);
            return result;
        } catch (Exception e) {
            long time = System.currentTimeMillis() - beginTime;
            logService.operationLog(request, annotation.value(), time, param, null, e);
            throw e;
        }
    }
}
