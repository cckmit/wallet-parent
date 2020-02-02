package org.wallet.gateway.client.service.dubbo;

import cn.jiguang.common.connection.IHttpClient;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.BaseResult;
import cn.jpush.api.JPushClient;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.wallet.common.dto.push.PushMessage;
import org.wallet.common.dto.push.PushTarget;
import org.wallet.dap.common.dubbo.*;
import org.wallet.gateway.client.utils.PushUtil;

import java.util.Collections;

/**
 * 极光推送服务
 * @author zengfucheng
 **/
@Slf4j
@Service(group = DubboServiceGroup.CLIENT_JIGUANG)
@org.springframework.stereotype.Service
public class JiGuangService extends BaseDubboService implements IService {

    @Autowired
    JPushClient pushClient;

    @Autowired
    IHttpClient pushHttpClient;

    public ServiceResponse push(ServiceRequest request, ServiceResponse response) {
        pushClient.getPushClient().setHttpClient(pushHttpClient);

        PushTarget target = request.getParamValue("target");
        PushMessage message = request.getParamValue("message");

        if(null == target){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[target]不存在！");
            return response;
        }else if(null == message){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[message]不存在！");
            return response;
        }

        try {
            BaseResult baseResult = pushClient.sendPush(PushUtil.buildPayload(target, message));
            response.setResult(PushUtil.covertResult(baseResult));
        } catch (APIConnectionException e) {
            log.warn("推送[{}][{}]失败[{}]次，稍后重试", JSON.toJSONString(target), JSON.toJSONString(message), e.getDoneRetriedTimes());
        } catch (APIRequestException e) {
            log.error("推送[{}][{}]失败：{}", JSON.toJSONString(target), JSON.toJSONString(message), e.getMessage(), e);
            response.setResult(PushUtil.covertException(e));
        }

        return response;
    }

    public ServiceResponse setAlias(ServiceRequest request, ServiceResponse response) {
        pushClient.getPushClient().setHttpClient(pushHttpClient);

        String clientId = request.getParamValue("clientId");
        String alias = request.getParamValue("alias");

        if(StringUtils.isEmpty(clientId)){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[clientId]不存在！");
            return response;
        }else if (StringUtils.isEmpty(alias)){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[alias]不存在！");
            return response;
        }

        try {
            pushClient.updateDeviceTagAlias(clientId, alias, null, null);
        } catch (APIConnectionException e) {
            log.warn("设置别名[{}][{}]失败[{}]次，稍后重试", clientId, alias, e.getDoneRetriedTimes());
        } catch (APIRequestException e) {
            log.warn("设置别名[{}][{}]失败：{}", clientId, alias, e.getMessage(), e);
            response.setResult(PushUtil.covertException(e));
        }

        return response;
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse removeTag(ServiceRequest request, ServiceResponse response) {
        pushClient.getPushClient().setHttpClient(pushHttpClient);

        String clientId = request.getParamValue("clientId");
        String tag = request.getParamValue("tag");

        if (StringUtils.isEmpty(tag)){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[tag]不存在！");
            return response;
        }else if(StringUtils.isEmpty(clientId)){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[clientId]不存在！");
            return response;
        }

        try {
            pushClient.addRemoveDevicesFromTag(tag, null, Collections.singleton(clientId));
        } catch (APIConnectionException e) {
            log.warn("删除标签[{}][{}]失败[{}]次，稍后重试", clientId, tag, e.getDoneRetriedTimes());
        } catch (APIRequestException e) {
            log.warn("删除标签[{}][{}]失败：{}", clientId, tag, e.getMessage(), e);
            response.setResult(PushUtil.covertException(e));
        }

        return response;
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse setTag(ServiceRequest request, ServiceResponse response) {
        pushClient.getPushClient().setHttpClient(pushHttpClient);

        String clientId = request.getParamValue("clientId");
        String tag = request.getParamValue("tag");

        if(StringUtils.isEmpty(clientId)){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[clientId]不存在！");
            return response;
        }else if (StringUtils.isEmpty(tag)){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[tag]不存在！");
            return response;
        }

        try {
            pushClient.addRemoveDevicesFromTag(tag, Collections.singleton(clientId), null);
        } catch (APIConnectionException e) {
            log.warn("设置标签[{}][{}]失败[{}]次，稍后重试", clientId, tag, e.getDoneRetriedTimes());
        } catch (APIRequestException e) {
            log.warn("设置标签[{}][{}]失败：{}", clientId, tag, e.getMessage(), e);
            response.setResult(PushUtil.covertException(e));
        }

        return response;
    }
}
