package org.wallet.dap.common.dubbo;

/**
 * @author zengfucheng
 */
public interface ResponseCode {
    /**
     * 成功
     */
    String SUCCESS = "0000000000";
    String SYS_ERROR = "9999";
    String SYS_BUSY = "8888";
    String NOT_FOUND = "404";
    String NOT_FOUND_DATA = "4040";
    String ILLEGAL_PARAM = "0001";
    String OVER_LIMIT = "0002";
    String DATA_DUPLICATE = "0003";
    String EXISTS_KEY_DATA = "0004";
    String UNAUTHORIZED = "0005";
    String FAIL = "0000000001";
    String RESULT_EXIST = "0000000009";

}
