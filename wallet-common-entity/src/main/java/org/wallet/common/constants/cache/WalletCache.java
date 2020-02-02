package org.wallet.common.constants.cache;

/**
 * @author zengfucheng
 **/
public interface WalletCache extends CacheConstants {
    String CACHE_PREFIX = "Wallet";
    String COUNT = "Count" + SEP + CACHE_PREFIX;
    String IMPORT = COUNT + SEP + "Import";
    String CREATE = COUNT + SEP + "Create";
}
