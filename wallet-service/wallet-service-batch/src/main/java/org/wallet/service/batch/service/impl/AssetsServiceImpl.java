package org.wallet.service.batch.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.wallet.common.constants.CoinConstants;
import org.wallet.common.constants.cache.QuotesCache;
import org.wallet.common.constants.cache.StatisticsCache;
import org.wallet.common.constants.field.AppChainField;
import org.wallet.common.constants.field.WalletCoinField;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.wallet.CoinAvgPriceDTO;
import org.wallet.common.entity.application.AppChainEntity;
import org.wallet.common.entity.wallet.WalletCoinEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.service.batch.dao.AppChainJpaDao;
import org.wallet.service.batch.dao.WalletCoinJpaDao;
import org.wallet.service.batch.service.AssetsService;
import org.wallet.service.common.bind.specification.MySpecification;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class AssetsServiceImpl implements AssetsService {

    @Autowired
    private Cache cache;

    @Autowired
    private AppChainJpaDao appChainJpaDao;

    @Autowired
    private WalletCoinJpaDao walletCoinJpaDao;

    @Override
    public void updateAssetsStatisticsData() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int day = calendar.get(Calendar.DAY_OF_YEAR);

        List<AppChainEntity> chainList = appChainJpaDao.findAll(new MySpecification<AppChainEntity>(
                Searchs.of(SearchFilters.eq(AppChainField.ENABLE, Boolean.TRUE))
        ).toPredicate());

        if(!CollectionUtils.isEmpty(chainList)){
            for(AppChainEntity entity : chainList){
                String chainCoinName = entity.getCoinName().toUpperCase();

                List<WalletCoinEntity> coinList = walletCoinJpaDao.findAll(new MySpecification<WalletCoinEntity>(
                        Searchs.of(SortDTO.asc(WalletCoinField.APP_CHAIN_ID, WalletCoinField.SORT),
                                SearchFilters.eq(WalletCoinField.ENABLE, Boolean.TRUE),
                                SearchFilters.eq(WalletCoinField.APP_CHAIN_ID, entity.getId()))
                ).toPredicate());

                String addressCountCache = StatisticsCache.ADDRESS_COUNT.replace(StatisticsCache.CHAIN, chainCoinName);

                Long addressCount = cache.sCard(addressCountCache);

                log.info("主链[{}]总地址数[{}]", addressCountCache, addressCount);

                addressCount = addressCount == null ? 0L : addressCount;

                // 将当前周和当日的总地址数量计入缓存，以便计算周环比和日环比
                cache.put(StatisticsCache.WEEKLY_ADDRESS + StatisticsCache.SEP + chainCoinName, String.valueOf(week), addressCount, StatisticsCache.EXPIRE_TWO_WEEK);
                cache.put(StatisticsCache.DAILY_ADDRESS + StatisticsCache.SEP + chainCoinName, String.valueOf(day), addressCount, StatisticsCache.EXPIRE_THREE_DAY);

                BigDecimal totalUSD = BigDecimal.ZERO;
                for(WalletCoinEntity coinEntity : coinList){
                    String contract = coinEntity.getContractAddress();
                    String coinName = coinEntity.getName().toUpperCase();
                    String totalCache = StatisticsCache.AMOUNT_CACHE.replace(StatisticsCache.CHAIN, chainCoinName)
                            .replace(StatisticsCache.CONTRACT, contract) + coinName;

                    BigDecimal coinTotal = cache.get(totalCache, StatisticsCache.TOTAL, BigDecimal.class);

                    log.info("币种[{}]总数量[{}]", totalCache + StatisticsCache.SEP + StatisticsCache.TOTAL, coinTotal);

                    if(null == coinTotal){ continue; }

                    String avgKey = QuotesCache.AVG + QuotesCache.SEP + chainCoinName + QuotesCache.SEP + contract;

                    CoinAvgPriceDTO avgPriceDTO = cache.get(avgKey, coinName, CoinAvgPriceDTO.class);

                    boolean isChain = coinName.equals(chainCoinName);

                    BigDecimal usdRate = BigDecimal.ZERO;

                    if(null != avgPriceDTO){
                        usdRate = avgPriceDTO.getAvgUSDRate();
                    }else if(isChain){
                        usdRate = cache.get(QuotesCache.PRICE_USD, chainCoinName, BigDecimal.class);
                    }

                    if(null != usdRate){
                        BigDecimal coinTotalUSD = coinTotal.multiply(usdRate);

                        log.info("币种[{}]总资产[$ {}] = {} * {}", totalCache + StatisticsCache.SEP + StatisticsCache.TOTAL,
                                coinTotalUSD, coinTotal, usdRate);

                        totalUSD = totalUSD.add(coinTotalUSD);
                    }
                }

                totalUSD = totalUSD.setScale(CoinConstants.SCALE_PRICE, BigDecimal.ROUND_DOWN);

                log.info("主链[{}]总资产[$ {}]", chainCoinName, totalUSD);

                // 将当前周和当日的总资产数量计入缓存，以便计算周环比和日环比
                cache.put(StatisticsCache.TOTAL_ASSETS, chainCoinName, totalUSD);
                cache.put(StatisticsCache.WEEKLY_ASSETS + StatisticsCache.SEP + chainCoinName, String.valueOf(week), totalUSD, StatisticsCache.EXPIRE_TWO_WEEK);
                cache.put(StatisticsCache.DAILY_ASSETS + StatisticsCache.SEP + chainCoinName, String.valueOf(day), totalUSD, StatisticsCache.EXPIRE_THREE_DAY);

                if(totalUSD.compareTo(BigDecimal.ZERO) > 0 && addressCount > 0){
                    BigDecimal avgAssets = totalUSD.divide(new BigDecimal(addressCount), CoinConstants.SCALE_PRICE, BigDecimal.ROUND_DOWN);

                    // 将人均资产放入缓存
                    cache.put(StatisticsCache.AVG_ASSETS, chainCoinName, avgAssets, StatisticsCache.EXPIRE_THREE_DAY);
                }
            }
        }
    }
}
