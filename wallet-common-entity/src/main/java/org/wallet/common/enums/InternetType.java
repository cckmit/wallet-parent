package org.wallet.common.enums;

/**
 * 网络类型
 * @author zengfucheng
 **/
public enum InternetType {
    /**
     * 移动2G网络
     */
    MOBILE_2G,
    /**
     * 移动3G网络
     */
    MOBILE_3G,
    /**
     * 移动4G网络
     */
    MOBILE_4G,
    /**
     * 移动5G网络
     */
    MOBILE_5G,
    /**
     * WIFI
     */
    WIFI,
    /**
     * UNKNOWN
     */
    UNKNOWN;

    public static InternetType getEnum(String value){
        return InternetType.valueOf(value);
    }
}
