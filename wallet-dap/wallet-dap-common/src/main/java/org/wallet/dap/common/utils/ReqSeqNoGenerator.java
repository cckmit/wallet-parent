package org.wallet.dap.common.utils;

/**
 * 请求流水号生成器
 * @author zengfucheng
 * @date 2018年3月9日
 */
public class ReqSeqNoGenerator {

	private final static String hostIpHex = Long.toHexString(Long.parseLong(HostUtil.getHostIp().replace(".", "")));
	
	public static String getReqSeqNo() {
		String sb = hostIpHex +
				Long.toHexString(System.currentTimeMillis()) +
				Long.toHexString((long) (Math.random() * 1000000000000000L));
		return sb;
	}
}
