package org.wallet.dap.mq;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.common.message.Message;

import java.io.UnsupportedEncodingException;

/**
 * @author zengfucheng
 * @date 2018年7月18日
 */
public class MessageWrapper extends Message {

	private static final long serialVersionUID = 6194572531059253370L;
	
	private static final String CHARSET = "UTF-8";
		
	private Object bodyObject;

	public MessageWrapper() {
		super();
	}

	public MessageWrapper(String topic, Object body) {
		 this(topic, "", "", 0, body, true);
	}

	public MessageWrapper(String topic, String tags, Object body) {
		  this(topic, tags, "", 0, body, true);
	}

	public MessageWrapper(String topic, String tags, String keys, Object body) {
		this(topic, tags, keys, 0, body, true);
	}

	public MessageWrapper(String topic, String tags, String keys, int flag, Object body, boolean waitStoreMsgOK) {
		super();
		this.bodyObject = body;
		setWaitStoreMsgOK(waitStoreMsgOK);
		setFlag(flag);
		setTags(tags);
		setKeys(keys);
		setTopic(topic);
		setBody(convertBytes(body));
	}

	public String getBodyJson() {
		return convertJson(bodyObject);
	}
	
	public void setBodyJson(String bodyJson) {
		setBody(convertBytes(bodyJson));
	}

	public Object getBodyObject() {
		return bodyObject;
	}
	
	public void setBodyObject(Object bodyObject) {
		this.bodyObject = bodyObject;
		setBody(convertBytes(bodyObject));
	}

	private String convertJson(Object bodyObject) {
		if(bodyObject instanceof String) {
			 return (String) bodyObject;
		 } else {
			 return JSON.toJSONString(bodyObject);
		 }
	}
	
	private byte[] convertBytes(Object bodyObject) {
		try {
			return convertJson(bodyObject).getBytes(CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	 }
	
	
}
