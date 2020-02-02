package org.wallet.common.constants.cache;

/**
 * @author zengfucheng
 **/
public interface WalletBlockTransCache extends CacheConstants {
    String CACHE_PREFIX = "WalletBlockTrans";
    String KEY_SYNC_BLOCK_TRANS = CACHE_PREFIX + SEP + KEY + SEP + "SyncTrans";
}
