package org.wallet.gateway.client.sms;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

/**
 * @author zengfucheng
 * @date 2018年8月3日
 */
@Component
public class SmsConfig implements InitializingBean{

	@Value("${sms.text.url}")
	private String textMsgUrl;
	
	@Value("${sms.text.account}")
	private String textMsgAccount;
	
	@Value("${sms.text.password}")
	private String textMsgPassword;
	
	@Value("${sms.voice.url}")
	private String voiceMsgUrl;
	
	@Value("${sms.voice.account}")
	private String voiceMsgAccount;
	
	@Value("${sms.voice.password}")
	private String voiceMsgPassword;
	
	@Value("${sms.connectionTimeout}")
	private int connectionTimeout;
	
	@Value("${sms.readTimeout}")
	private int readTimeout;
	
	@Value("${sms.authExpire}")
	private int authExpire;
	
	@Value("${sms.debugEnable}")
	private boolean debug;
	
	@Value("${sms.debugCode}")
	private String debugCode;
	
	@Value("${sms.message}")
	private String message;

	public String getTextMsgUrl() {
		return textMsgUrl;
	}

	public void setTextMsgUrl(String textMsgUrl) {
		this.textMsgUrl = textMsgUrl;
	}

	public String getTextMsgAccount() {
		return textMsgAccount;
	}

	public void setTextMsgAccount(String textMsgAccount) {
		this.textMsgAccount = textMsgAccount;
	}

	public String getTextMsgPassword() {
		return textMsgPassword;
	}

	public void setTextMsgPassword(String textMsgPassword) {
		this.textMsgPassword = textMsgPassword;
	}

	public String getVoiceMsgUrl() {
		return voiceMsgUrl;
	}

	public void setVoiceMsgUrl(String voiceMsgUrl) {
		this.voiceMsgUrl = voiceMsgUrl;
	}

	public String getVoiceMsgAccount() {
		return voiceMsgAccount;
	}

	public void setVoiceMsgAccount(String voiceMsgAccount) {
		this.voiceMsgAccount = voiceMsgAccount;
	}

	public String getVoiceMsgPassword() {
		return voiceMsgPassword;
	}

	public void setVoiceMsgPassword(String voiceMsgPassword) {
		this.voiceMsgPassword = voiceMsgPassword;
	}
	
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public int getAuthExpire() {
		return authExpire;
	}

	public void setAuthExpire(int authExpire) {
		this.authExpire = authExpire;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getDebugCode() {
		return debugCode;
	}

	public void setDebugCode(String debugCode) {
		this.debugCode = debugCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		setTextMsgAccount(new String(Base64.getDecoder().decode(textMsgAccount+"=")));
		setTextMsgPassword(new String(Base64.getDecoder().decode(textMsgPassword+"=")));
		setVoiceMsgAccount(new String(Base64.getDecoder().decode(voiceMsgAccount+"=")));
		setVoiceMsgPassword(new String(Base64.getDecoder().decode(voiceMsgPassword+"=")));
	}
	
}
