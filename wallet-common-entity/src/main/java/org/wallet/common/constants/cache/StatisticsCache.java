package org.wallet.common.constants.cache;

import java.time.Duration;

public interface StatisticsCache extends CacheConstants{
    /** 两周的缓存过期时间 **/
    Long EXPIRE_TWO_WEEK = Duration.ofDays(14).getSeconds();
    Long EXPIRE_THREE_DAY = Duration.ofDays(3).getSeconds();
    /** 缓存前缀 */
    String CACHE_PREFIX = "Statistics";
    /** 资产 */
    String ASSETS = CACHE_PREFIX + SEP + "Assets";
    /** 总资产 */
    String TOTAL_ASSETS = ASSETS + SEP + "Total";
    /** 总资产（每周记录） */
    String WEEKLY_ASSETS = ASSETS + SEP + "Week";
    /** 总资产（每天记录） */
    String DAILY_ASSETS = ASSETS + SEP + "Day";
    /** 日均新增资产 */
    String DAILY_AVG_ASSETS = ASSETS + SEP + "Avg:Day";
    /** 人均资产 */
    String AVG_ASSETS = ASSETS + SEP + "Avg";
    /** 地址 */
    String ADDRESS = CACHE_PREFIX + SEP + "Address";
    /** 总地址数 */
    String TOTAL_ADDRESS = ADDRESS + SEP + "Total";
    /** 总地址数（每周记录） */
    String WEEKLY_ADDRESS = ADDRESS + SEP + "Week";
    /** 总地址数（每天记录） */
    String DAILY_ADDRESS = ADDRESS + SEP + "Day";
    /** 日均新增地址 */
    String DAILY_AVG_ADDRESS = ADDRESS + SEP + "Avg";

    /* ------------------------ 以下缓存来自区块链服务器 ------------------------ **/

    /*
     * 合约币种资产总计
     * {EOS}:athenastoken:ATHENA:total
     * 其中EOS为主链，athenastoken为币种合约，ATHENA为币种
     **/
    /*
     * 合约账户总数
     * UJP:EOS:0
     * 其中EOS为主链
     **/

    /** 主链 */
    String CHAIN = "chain";
    /** 合约 */
    String CONTRACT = "contract";
    /** 拼接用，无意义 */
    String AMOUNT_CACHE = "{chain}:contract:";
    /** 拼接用，无意义 */
    String TOTAL = "total";
    /** 拼接用，无意义 */
    String ADDRESS_COUNT = "UJP:chain:0";
}
