package org.wallet.web.common.mvc.version;

public enum MatchType {
    /**
     * 全部匹配
     */
    ALL,
    /**
     * 小于版本号
     */
    LESS_THAN,
    /**
     * 大于版本号
     */
    MORE_THAN,
    /**
     * 等于版本号
     */
    EQUAL
}
