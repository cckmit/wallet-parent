package org.wallet.web.common.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.dubbo.ResponseCode;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.dap.common.exception.DataNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zengfucheng
 **/
public abstract class BaseController {
    protected Logger log = LoggerFactory.getLogger(getClass());

    @Value("${spring.application.name}")
    protected String appName;

    protected String serviceGroup;

    @Autowired
    protected Cache cache;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    protected void setServiceGroup(String serviceGroup){
        this.serviceGroup = serviceGroup;
    }

    protected ServiceRequest createRequest(String methodName){
        return ServiceRequest.newInstance(appName, serviceGroup, methodName);
    }

    protected ServiceRequest createRequest(String methodName, Long userId){
        return ServiceRequest.newInstance(appName, serviceGroup, methodName, userId);
    }

    protected ServiceRequest createRequest(String methodName, Long userId, Object param){
        return ServiceRequest.newInstance(appName, serviceGroup, methodName, userId, param);
    }

    protected ServiceRequest createRequest(String serviceGroup, String methodName){
        return ServiceRequest.newInstance(appName, serviceGroup, methodName);
    }

    protected ServiceRequest createRequest(String serviceGroup, String methodName, Object param){
        return ServiceRequest.newInstance(appName, serviceGroup, methodName, null, param);
    }

    protected void handleFailResponse(ServiceResponse response){
        if(null == response){ return; }
        if(ResponseCode.NOT_FOUND_DATA.equals(response.getRespCode())){ throw new DataNotFoundException(response.getRespMsg()); }
    }
}
