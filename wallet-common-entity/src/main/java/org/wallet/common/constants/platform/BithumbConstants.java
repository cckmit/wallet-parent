package org.wallet.common.constants.platform;

/**
 * Bithumb 接口参数常量
 */
public interface BithumbConstants {
    /**
     * 响应成功的返回码
     */
    String STATUS_SUCCESS = "0";

    /**
     * 异常响应码
     * 0        success
     * 9000	    missing parameter
     * 9001	    version not matched
     * 9002	    verifySignature failed
     * 9004	    access denied
     * 9005	    key expired
     * 9006	    no server
     * 9999	    system error
     * 20043	price accuracy is wrong for placing order
     * 20044	quantity accuracy is wrong for placing order
     */
}
