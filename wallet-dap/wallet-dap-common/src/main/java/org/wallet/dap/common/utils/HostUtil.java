package org.wallet.dap.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 
 * @author zengfucheng
 * @date 2018年3月8日
 */
public class HostUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(HostUtil.class);
	
	private static String hostIp;
	private static String hostName;
	
	static {
		try {
			InetAddress host = InetAddress.getLocalHost();
			hostIp = host.getHostAddress();
			hostName = host.getHostName();
		} catch (UnknownHostException e) {
			LOGGER.error("获取服务器信息异常", e);
		}
	}
	
	public static String getHostIp() {
		return hostIp;
	}
	
	public static String getHostName() {
		return hostName;
	}
}
