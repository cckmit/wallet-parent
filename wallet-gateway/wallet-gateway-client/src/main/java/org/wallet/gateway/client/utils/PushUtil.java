package org.wallet.gateway.client.utils;

import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.BaseResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;
import org.wallet.common.dto.push.PushMessage;
import org.wallet.common.dto.push.PushResult;
import org.wallet.common.dto.push.PushTarget;
import org.wallet.common.enums.Device;

/**
 * @author zengfucheng
 **/
public class PushUtil {
    /**
     * 构建推送目标
     * @param target 目标
     * @return Audience
     */
    public static Audience buildAudience(PushTarget target){
        Audience.Builder builder = Audience.newBuilder();

        if(!CollectionUtils.isEmpty(target.getClientIds())){
            return Audience.registrationId(target.getClientIds());
        }else{
            builder.setAll(target.getDevice().equals(Device.ALL));
            if(!CollectionUtils.isEmpty(target.getAlias())){
                builder.addAudienceTarget(AudienceTarget.alias(target.getAlias()));
            }
            if(!CollectionUtils.isEmpty(target.getTags())){
                builder.addAudienceTarget(AudienceTarget.tag(target.getAlias()));
            }
        }

        return builder.build();
    }

    /**
     * 构建推送载体
     * @param target 目标
     * @param message 消息
     * @return 推送内容
     */
    public static PushPayload buildPayload(PushTarget target, PushMessage message){
        PushPayload.Builder builder = new PushPayload.Builder();

        Device device = target.getDevice();

        builder.setPlatform(covertPlatform(device));
        builder.setAudience(buildAudience(target));
        builder.setNotification(buildNotify(device, message));

        return builder.build();
    }

    /**
     * 构建推送内容
     * @param device 设备
     * @param message 消息
     * @return 内容
     */
    public static Notification buildNotify(Device device, PushMessage message){
        if(device.equals(Device.IOS)){
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(message.getContent()));
            jsonObject.put("title", message.getTitle());
            return Notification.ios(jsonObject, message.mapData());
        }else if(device.equals(Device.ANDROID)){
            return Notification.android(message.getContent().toString(), message.getTitle(), message.mapData());
        }else{
            Notification.Builder builder = Notification.newBuilder();
            if(null != message.getContent()){
                builder.setAlert(message.getContent());
            }
            builder.addPlatformNotification(AndroidNotification.newBuilder().setTitle(message.getTitle()).addExtras(message.mapData()).build());
            builder.addPlatformNotification(IosNotification.newBuilder().addExtras(message.mapData()).build());
            return builder.build();
        }
    }

    /**
     * 转换响应结果
     * @param baseResult 响应结果
     * @return 结果
     */
    public static PushResult covertResult(BaseResult baseResult){
        PushResult result = new PushResult();
        result.setCode(baseResult.getResponseCode());
        result.setContent(baseResult.getOriginalContent());
        result.setQuota(baseResult.getRateLimitQuota());
        result.setRemain(baseResult.getRateLimitRemaining());
        result.setReset(baseResult.getRateLimitReset());
        return result;
    }

    /**
     * 转换异常
     * @param e 异常
     * @return 结果
     */
    public static PushResult covertException(APIRequestException e){
        PushResult errorResult = new PushResult();
        errorResult.setQuota(e.getRateLimitQuota());
        errorResult.setRemain(e.getRateLimitRemaining());
        errorResult.setReset(e.getRateLimitReset());
        PushResult.Error error = new PushResult.Error();
        error.setCode(e.getErrorCode());
        error.setMsg(e.getErrorMessage());
        error.setMsgId(e.getMsgId());
        errorResult.setError(error);
        return errorResult;
    }

    /**
     * 转换平台
     * @param device 设备
     * @return 极光平台
     */
    public static Platform covertPlatform(Device device){
        switch (device){
            case ALL: return Platform.android_ios();
            case IOS: return Platform.ios();
            case ANDROID: return Platform.android();
            default: return null;
        }
    }
}
