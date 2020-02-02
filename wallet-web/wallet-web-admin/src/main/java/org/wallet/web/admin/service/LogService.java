package org.wallet.web.admin.service;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.MQTopic;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.admin.SysErrorLogDTO;
import org.wallet.common.dto.admin.SysLoginLogDTO;
import org.wallet.common.dto.admin.SysOperationLogDTO;
import org.wallet.common.dto.admin.SysUserDTO;
import org.wallet.common.enums.admin.LoginOperationEnum;
import org.wallet.common.enums.admin.LoginStatusEnum;
import org.wallet.common.enums.admin.OperationStatusEnum;
import org.wallet.dap.mq.MQException;
import org.wallet.dap.mq.core.RMQSendingTemplate;
import org.wallet.web.admin.utils.UserUtil;
import org.wallet.web.common.mvc.controller.ErrorHandler;
import org.wallet.web.common.utils.SignUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
public class LogService implements ErrorHandler {

    @Autowired
    private RMQSendingTemplate sendingTemplate;

    public void loginLog(HttpServletRequest request, Long userId, String username, LoginOperationEnum operation, LoginStatusEnum status){
        SysLoginLogDTO loginLog = new SysLoginLogDTO();

        String ip = ServletUtil.getClientIP(request);

        loginLog.setOperation(operation.value());
        loginLog.setCreateDate(new Date());
        loginLog.setIp(ServletUtil.getClientIP(request));
        loginLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        loginLog.setCreator(userId);
        loginLog.setCreatorName(username);
        loginLog.setStatus(status.value());

        try{
            sendingTemplate.syncSend(MQTopic.ADMIN_LOGIN_LOG, loginLog);
        } catch (MQException mq){
            log.warn("发送登录日志异常：{}\r\n登录日志：{}", mq.getMessage(), JSON.toJSONString(loginLog));
        }
    }

    @Override
    public void handleException(HttpServletRequest request, Throwable e) {
        SysErrorLogDTO errorLog = new SysErrorLogDTO();

        errorLog.setIp(ServletUtil.getClientIP(request));
        errorLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        errorLog.setRequestUri(request.getRequestURI());
        errorLog.setRequestMethod(request.getMethod());
        try {
            errorLog.setRequestParams(JSON.toJSONString(SignUtil.getRequestParamMap(request)));
        } catch (IOException ignored) {}
        errorLog.setErrorInfo(ExceptionUtil.stacktraceToString(e));
        errorLog.setCreator(UserUtil.getUser().getId());
        errorLog.setCreateDate(new Date());

        try{
            sendingTemplate.syncSend(MQTopic.ADMIN_ERROR_LOG, errorLog);
        } catch (MQException mq){
            log.warn("发送操作失败日志异常：{}\r\n操作失败日志：{}", mq.getMessage(), JSON.toJSONString(errorLog));
        }
    }

    public void operationLog(HttpServletRequest request, String operation, long time, Object firstParam, Object result, Exception e) {
        SysOperationLogDTO logDTO = new SysOperationLogDTO();
        OperationStatusEnum status = OperationStatusEnum.SUCCESS;

        SysUserDTO user = UserUtil.getUser();
        if(user != null){
            logDTO.setCreator(user.getId());
            logDTO.setCreatorName(user.getUsername());
        }

        if(e != null){
            status = OperationStatusEnum.EXCEPTION;
            if(null == result){ result = ExceptionUtil.stacktraceToString(e); }
        }else if(result instanceof SimpleResult){
            SimpleResult simpleResult = (SimpleResult) result;
            status = simpleResult.getSuccess() ? OperationStatusEnum.SUCCESS : OperationStatusEnum.FAIL;
        }

        logDTO.setOperation(operation);
        logDTO.setRequestTime((int)time);

        logDTO.setIp(ServletUtil.getClientIP(request));
        logDTO.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        logDTO.setRequestUri(request.getRequestURI());
        logDTO.setRequestMethod(request.getMethod());
        logDTO.setRequestParams(JSON.toJSONString(firstParam));
        logDTO.setResponseBody(JSON.toJSONString(result));
        logDTO.setStatus(status.value());
        logDTO.setCreateDate(new Date());

        try{
            sendingTemplate.syncSend(MQTopic.ADMIN_OPERATION_LOG, logDTO);
        } catch (MQException mq){
            log.warn("发送操作日志异常：{}\r\n操作日志：{}", mq.getMessage(), JSON.toJSONString(logDTO));
        }
    }
}
