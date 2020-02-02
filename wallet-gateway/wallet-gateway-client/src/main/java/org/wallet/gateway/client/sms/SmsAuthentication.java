package org.wallet.gateway.client.sms;

import com.alibaba.dubbo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wallet.dap.cache.Cache;

/**
 * 短信认证
 * @author zengfucheng
 * @date 2018年8月3日
 */
@Component
public class SmsAuthentication {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private Cache cache;
	
	public boolean auth(String phoneNo, String code, String bizType) {
		String smsCode = cache.get(getCacheKey(bizType), phoneNo, String.class);
		if(StringUtils.isNotEmpty(smsCode) && code.equals(smsCode)){
			try {
				cache.evict(getCacheKey(bizType), phoneNo);
			} catch (Exception e) {
				logger.warn("", e);
			}
            return true;
        } else {
        	return false;
        }
	}
	
	private String getCacheKey(String bizType) {
		return AbstractSmsSender.SMS_CACHE_NAME+":"+bizType;
	}
}
