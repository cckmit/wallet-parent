package org.wallet.gateway.client.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.wallet.dap.cache.Cache;
import org.wallet.gateway.client.service.dubbo.SmsService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * 短信发送
 * @author zengfucheng
 * @date 2018年8月3日
 */
public abstract class AbstractSmsSender {

	private  Logger logger = LoggerFactory.getLogger(getClass());
	public static final String SMS_CACHE_NAME = "SMS";
	
	@Autowired
	protected SmsConfig config;
	
	@Autowired
	private Cache cache;
	//业务类型
	private String bizType;
	
	public String send(String type, String phoneNo, String content) {
		//发送验证码
		if(StringUtils.isEmpty(content)) {
			if(config.isDebug()) {
				createAuthCodeContent(type, phoneNo);
				return "0";
			}
		}
		return sendSMS(phoneNo, content);
	}
	
	public abstract String sendSMS(String phoneNo, String content);
	
	protected String sendSmsByPost(String path, String postContent) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(path).openConnection();
        // 提交模式
        httpURLConnection.setRequestMethod("POST");
        //连接超时 单位毫秒
        httpURLConnection.setConnectTimeout(config.getConnectionTimeout());
        //读取超时 单位毫秒
        httpURLConnection.setReadTimeout(config.getReadTimeout());
        // 发送POST请求必须设置如下两行
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestProperty("Charset", "UTF-8");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.connect();
        OutputStream os = httpURLConnection.getOutputStream();
        os.write(postContent.getBytes("UTF-8"));
        os.flush();

        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
		logger.info("sendSmsByPost result:{}",sb.toString());
        return sb.toString();
    }
	
	/**
	 * 创建验证码
	 *
	 * @param type
	 * @param phoneNo
	 * @return
	 */
	protected String createAuthCodeContent(String type, String phoneNo) {
		String code = null;
		if(config.isDebug()) {
			code = config.getDebugCode();
		} else {
			code = String.valueOf(new Random().nextInt(900000) + 99999);
		}
		logger.info("创建{}验证码 phoneNo = {} >>>>>>>>> code = {}", SmsService.SmsType.TEXT_CODE.equals(type) ? "短信" : "语音", phoneNo, code);
        cache.put(SMS_CACHE_NAME + ":" + getBizType(), phoneNo, code, config.getAuthExpire());
        if(SmsService.SmsType.TEXT_CODE.equals(type)){
        	return config.getMessage().replace("{}", code);
		}else{
			return code;
		}
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
}
