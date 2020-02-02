package org.wallet.common.constants.cache;

/**
 * @author zengfucheng
 **/
public interface WalletCoinOrderCache extends CacheConstants {
    String CACHE_PREFIX = "WalletCoinOrder";
    String KEY_FIX_ORDER = CACHE_PREFIX + SEP + KEY + SEP + "FixOrder";
}
