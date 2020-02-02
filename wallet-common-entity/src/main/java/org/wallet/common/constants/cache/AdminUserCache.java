package org.wallet.common.constants.cache;

public interface AdminUserCache extends CacheConstants{
    /** 缓存前缀 */
    String CACHE_PREFIX = "AdminUser";
    String PERMISSIONS = CACHE_PREFIX + SEP + "Permissions";
    String USER_BY_ID = CACHE_PREFIX + SEP + "Id";
    String USER_BY_USERNAME = CACHE_PREFIX + SEP + "Username";
    String DATA_SCOPE = CACHE_PREFIX + SEP + "DataScope";
}
