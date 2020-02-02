package org.wallet.gateway.client.sms;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

/**
 * 自定义文本发送
 * @author zengfucheng
 * @date 2018年8月3日
 */
@Component
public class SmsTextCustomSender extends AbstractSmsSender {

	@Override
	public String sendSMS(String phoneNo, String content) {
		try {
			return JSON.parseObject(sendSmsByPost(config.getTextMsgUrl(), 
					JSON.toJSONString(
							new SmsSendRequest(config.getTextMsgAccount(), 
									config.getTextMsgPassword(), content, phoneNo, "true")
					)
			), SmsSendResponse.class).getCode();
		} catch (Exception e) {
			throw new RuntimeException("send sms fialure", e);
		} 
	}

}
