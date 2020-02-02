package org.wallet.gateway.client.sms;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import org.wallet.gateway.client.service.dubbo.SmsService;

/**
 * 语音短信发送
 *
 * @author zengfucheng
 * @date 2018年8月3日
 */
@Component
public class SmsVoiceSender extends AbstractSmsSender {

    @Override
    public String sendSMS(String phoneNo, String content) {
        try {
            return JSON.parseObject(sendSmsByPost(config.getVoiceMsgUrl(),
                    JSON.toJSONString(
                            new VoiceSmsSendRequest(config.getVoiceMsgAccount(),
                                    config.getVoiceMsgPassword(), phoneNo, createAuthCodeContent(SmsService.SmsType.VOICE_CODE, phoneNo))
                    )
            ), SmsSendResponse.class).getCode();
        } catch (Exception e) {
            throw new RuntimeException("send voice sms fail", e);
        }
    }

}
