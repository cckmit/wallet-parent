package org.wallet.service.application.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.CoinConstants;
import org.wallet.common.constants.cache.StatisticsCache;
import org.wallet.common.dto.admin.AssetsStatisticsDTO;
import org.wallet.common.entity.application.AppChainEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.service.application.dao.AppChainJpaDao;
import org.wallet.service.application.dao.WalletCoinJpaDao;
import org.wallet.service.application.service.StatisticsService;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private Cache cache;

    @Autowired
    private AppChainJpaDao appChainJpaDao;

    @Autowired
    private WalletCoinJpaDao walletCoinJpaDao;

    @Override
    public AssetsStatisticsDTO assetsStatistics(Long chainId) {
        AppChainEntity chainEntity = appChainJpaDao.getOne(chainId);

        String chainCoinName = chainEntity.getCoinName();

        if(null == chainEntity){ return null; }

        AssetsStatisticsDTO dto = new AssetsStatisticsDTO();

        // 获取实时主链地址总数
        String addressCountCache = StatisticsCache.ADDRESS_COUNT.replace(StatisticsCache.CHAIN, chainCoinName);

        Long totalAddress = cache.sCard(addressCountCache);

        log.info("主链[{}]总地址数[{}]", chainCoinName, totalAddress);

        totalAddress = totalAddress == null ? 0L : totalAddress;

        // 获取缓存主链资产总数
        BigDecimal totalAssets = cache.get(StatisticsCache.TOTAL_ASSETS, chainCoinName, BigDecimal.class);

        log.info("主链[{}]总资产[{}]", chainCoinName, totalAssets);

        totalAssets = totalAssets == null ? BigDecimal.ZERO : totalAssets;

        BigDecimal avgAssets = BigDecimal.ZERO;

        if(totalAssets.compareTo(BigDecimal.ZERO) > 0 && totalAddress > 0){
            avgAssets = totalAssets.divide(new BigDecimal(totalAddress), CoinConstants.SCALE_PRICE, BigDecimal.ROUND_DOWN);
        }
        dto.setAvgAssets(avgAssets);

        // 获取上周数字
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        int lastWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        // 获取昨天数字
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        int lastDay = calendar.get(Calendar.DAY_OF_YEAR);

        // 获取上周和昨天总资产
        BigDecimal lastWeekAssets = cache.get(StatisticsCache.WEEKLY_ASSETS + StatisticsCache.SEP + chainCoinName, String.valueOf(lastWeek), BigDecimal.class);
        BigDecimal lastDayAssets = cache.get(StatisticsCache.DAILY_ASSETS + StatisticsCache.SEP + chainCoinName, String.valueOf(lastDay), BigDecimal.class);

        log.info("主链[{}]上周总资产[{}]", chainCoinName, lastWeekAssets);
        log.info("主链[{}]昨日总资产[{}]", chainCoinName, lastDayAssets);

        lastWeekAssets = null == lastWeekAssets ? totalAssets : lastWeekAssets;
        lastDayAssets = null == lastDayAssets ? totalAssets : lastDayAssets;

        // 计算总资产周环比和日环比
        BigDecimal assetsWeeklyRingRatio = BigDecimal.ZERO;
        if(lastWeekAssets.compareTo(BigDecimal.ZERO) > 0){
            assetsWeeklyRingRatio = totalAssets.divide(lastWeekAssets, CoinConstants.SCALE_PERCENT, BigDecimal.ROUND_DOWN).subtract(BigDecimal.ONE);
        }
        BigDecimal assetsDailyRingRatio = BigDecimal.ZERO;
        if(lastDayAssets.compareTo(BigDecimal.ZERO) > 0){
            assetsDailyRingRatio = totalAssets.divide(lastDayAssets, CoinConstants.SCALE_PERCENT, BigDecimal.ROUND_DOWN).subtract(BigDecimal.ONE);
        }

        // 获取上周和昨天总地址数
        Long lastWeekAddress = cache.get(StatisticsCache.WEEKLY_ADDRESS + StatisticsCache.SEP + chainCoinName, String.valueOf(lastWeek), Long.class);
        Long lastDayAddress = cache.get(StatisticsCache.DAILY_ADDRESS + StatisticsCache.SEP + chainCoinName, String.valueOf(lastDay), Long.class);

        log.info("主链[{}]上周总地址[{}]", chainCoinName, lastWeekAddress);
        log.info("主链[{}]昨日总地址[{}]", chainCoinName, lastDayAddress);

        lastWeekAddress = null == lastWeekAddress ? totalAddress : lastWeekAddress;
        lastDayAddress = null == lastDayAddress ? totalAddress : lastDayAddress;

        // 计算总地址数周环比和日环比
        BigDecimal addressWeeklyRingRatio = BigDecimal.ZERO;
        if(lastWeekAddress > 0){
            addressWeeklyRingRatio = new BigDecimal(totalAddress).divide(new BigDecimal(lastWeekAddress), CoinConstants.SCALE_PERCENT, BigDecimal.ROUND_DOWN);
            if(addressWeeklyRingRatio.compareTo(BigDecimal.ONE) >= 0){
                addressWeeklyRingRatio = addressWeeklyRingRatio.subtract(BigDecimal.ONE);
            }
        }
        BigDecimal addressDailyRingRatio = BigDecimal.ZERO;
        if(lastDayAddress > 0){
            addressDailyRingRatio = new BigDecimal(totalAddress).divide(new BigDecimal(lastDayAddress), CoinConstants.SCALE_PERCENT, BigDecimal.ROUND_DOWN);
            if(addressDailyRingRatio.compareTo(BigDecimal.ONE) >= 0){
                addressDailyRingRatio = addressDailyRingRatio.subtract(BigDecimal.ONE);
            }
        }

        dto.setTotalAssets(totalAssets);
        dto.setAssetsWeeklyRingRatio(assetsWeeklyRingRatio);
        dto.setAssetsDailyRingRatio(assetsDailyRingRatio);
        dto.setAssetsDaily(totalAssets.subtract(lastDayAssets));

        dto.setTotalAddress(totalAddress);
        dto.setAddressWeeklyRingRatio(addressWeeklyRingRatio);
        dto.setAddressDailyRingRatio(addressDailyRingRatio);
        dto.setAddressDaily(totalAddress - lastDayAddress);

        return dto;
    }
}
