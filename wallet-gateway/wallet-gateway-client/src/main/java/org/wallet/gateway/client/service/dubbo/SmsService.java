package org.wallet.gateway.client.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.wallet.dap.common.dubbo.*;
import org.wallet.gateway.client.sms.SmsAuthentication;
import org.wallet.gateway.client.sms.SmsTextCodeSender;
import org.wallet.gateway.client.sms.SmsTextCustomSender;
import org.wallet.gateway.client.sms.SmsVoiceSender;

/**
 * 短信服务
 * @author zengfucheng
 */
@Service(group = DubboServiceGroup.CLIENT_SMS)
@org.springframework.stereotype.Service
public class SmsService extends BaseDubboService implements IService {
	@Autowired
    SmsAuthentication smsAuth;
	@Autowired
    SmsTextCodeSender textCodeSender;
	@Autowired
    SmsTextCustomSender customSender;
	@Autowired
    SmsVoiceSender voiceSender;

    public static class SmsType {
        public static final String TEXT_CODE = "1";
        public static final String VOICE_CODE = "2";
        public static final String CUSTOM_TEXT = "3";
    }

	public ServiceResponse send(ServiceRequest request, ServiceResponse response) {
		String phoneNo = request.getParamValue("phoneNo");
		String type = request.getParamValue("type");

		if (StringUtils.isEmpty(phoneNo)) {
			response.setRespCode(ResponseCode.ILLEGAL_PARAM);
			response.setRespMsg("phoneNo is null");
			return response;
		}

		if (StringUtils.isEmpty(type)) {
			response.setRespCode(ResponseCode.ILLEGAL_PARAM);
			response.setRespMsg("type is null");
			return response;
		}

		String bizType = null;
		if (type.equals(SmsType.TEXT_CODE) || type.equals(SmsType.VOICE_CODE)) {
			bizType = request.getParamValue("bizType");
			if (StringUtils.isEmpty(bizType)) {
				response.setRespCode(ResponseCode.ILLEGAL_PARAM);
				response.setRespMsg("bizType is null");
				return response;
			}
		}
		String result;
		switch (type) {
			case SmsType.TEXT_CODE:
				textCodeSender.setBizType(bizType);
				result = textCodeSender.send(SmsType.TEXT_CODE, phoneNo, null);
				break;
			case SmsType.VOICE_CODE:
				voiceSender.setBizType(bizType);
				result = voiceSender.send(SmsType.VOICE_CODE, phoneNo, null);
				break;
			case SmsType.CUSTOM_TEXT:
				String content = request.getParamValue("content");
				if (StringUtils.isEmpty(content)) {
					response.setRespCode(ResponseCode.ILLEGAL_PARAM);
					response.setRespMsg("content is null");
					return response;
				}
				result = customSender.send(SmsType.CUSTOM_TEXT, phoneNo, content);
				break;
			default:
				response.setRespCode(ResponseCode.ILLEGAL_PARAM);
				response.setRespMsg("type dose not exist");
				return response;
		}

		if ("0".equals(result)) {
			response.setRespCode(ResponseCode.SUCCESS);
			response.setRespMsg("send sms success!");
		} else {
			response.setRespCode(ResponseCode.FAIL);
			response.setRespMsg("send sms failure!");
		}
		return response;
	}

	public ServiceResponse auth(ServiceRequest request, ServiceResponse response) {
		String phoneNo = request.getParamValue("phoneNo");
		String code = request.getParamValue("code");
		String bizType = request.getParamValue("bizType");
		if(StringUtils.isEmpty(phoneNo)) {
			response.setRespCode(ResponseCode.ILLEGAL_PARAM);
			response.setRespMsg("phoneNo is null");
			return response;
		}

		if(StringUtils.isEmpty(code)) {
			response.setRespCode(ResponseCode.ILLEGAL_PARAM);
			response.setRespMsg("code is null");
			return response;
		}

		if(StringUtils.isEmpty(bizType)) {
			response.setRespCode(ResponseCode.ILLEGAL_PARAM);
			response.setRespMsg("bizType is null");
			return response;
		}

		if(smsAuth.auth(phoneNo, code, bizType)) {
			response.setRespCode(ResponseCode.SUCCESS);
			response.setRespMsg("sms auth success");
		} else {
			response.setRespCode(ResponseCode.FAIL);
			response.setRespMsg("sms auth fail");
		}
		return response;
	}


}
