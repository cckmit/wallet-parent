package org.wallet.common.constants.cache;

import java.time.Duration;

/**
 * @author zengfucheng
 **/
public interface CacheConstants {
    /** 缓存分隔符 */
    String SEP = ":";
    /** Spring CacheName 缓存分隔符 */
    String SPRING_SEP = SEP + SEP;
    /** 默认缓存超时时间（2 小时） */
    Long EXPIRE = Duration.ofHours(2).getSeconds();
    /** 树级结构缓存 */
    String TREE = "TREE";
    /** 当前实体所有资源 */
    String ALL = "All";
    /** 分布式锁前缀 */
    String KEY = "Key";
}
