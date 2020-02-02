package org.wallet.gateway.client.sms;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import org.wallet.gateway.client.service.dubbo.SmsService;

/**
 * 验证码发送
 * @author zengfucheng
 * @date 2018年8月3日
 */
@Component
public class SmsTextCodeSender extends AbstractSmsSender {

	@Override
	public String sendSMS(String phoneNo, String content) {
		try {
			return JSON.parseObject(sendSmsByPost(config.getTextMsgUrl(),
					JSON.toJSONString(
							new SmsSendRequest(config.getTextMsgAccount(), 
									config.getTextMsgPassword(), createAuthCodeContent(SmsService.SmsType.TEXT_CODE, phoneNo), phoneNo, "true")
					)
			), SmsSendResponse.class).getCode();
		} catch (Exception e) {
			throw new RuntimeException("send sms fialure",e);
		}
	}

}
