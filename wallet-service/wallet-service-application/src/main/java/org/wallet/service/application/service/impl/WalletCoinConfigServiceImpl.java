package org.wallet.service.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.cache.QuotesCache;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.wallet.CoinAvgPriceDTO;
import org.wallet.common.dto.wallet.ExchangePriceDTO;
import org.wallet.common.dto.wallet.QuotesDTO;
import org.wallet.common.dto.wallet.WalletCoinPriceDTO;
import org.wallet.common.entity.wallet.WalletCoinConfigEntity;
import org.wallet.common.enums.wallet.QuotesSourceEnum;
import org.wallet.service.application.dao.WalletCoinConfigJpaDao;
import org.wallet.service.application.dao.WalletCoinJpaDao;
import org.wallet.service.application.service.WalletCoinConfigService;
import org.wallet.service.common.bind.JPACovert;
import org.wallet.service.common.service.AbstractCrudService;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zengfucheng
 **/
@Service
public class WalletCoinConfigServiceImpl extends AbstractCrudService<WalletCoinConfigJpaDao, WalletCoinConfigEntity> implements WalletCoinConfigService {

    @Autowired
    private WalletCoinJpaDao walletCoinJpaDao;

    @Override
    public PageDTO<WalletCoinPriceDTO> findWalletCoinPrice(Long chainId, PageDTO<WalletCoinPriceDTO> pageDTO) {
        Page<WalletCoinPriceDTO> page = walletCoinJpaDao.findPriceDTOList(chainId, JPACovert.covertPage(pageDTO));

        List<WalletCoinPriceDTO> priceDTOList = page.getContent();

        if(!CollectionUtils.isEmpty(priceDTOList)){
            for(WalletCoinPriceDTO coinPriceDTO : priceDTOList){
                List<ExchangePriceDTO> exchangePriceDTOList = getRepository().getExchangeList(coinPriceDTO.getCoinId());

                String chainCoin = coinPriceDTO.getChainCoinName();
                String contract = coinPriceDTO.getContract();
                String coin = coinPriceDTO.getCoinName();

                if(!CollectionUtils.isEmpty(exchangePriceDTOList)){
                    for(ExchangePriceDTO exchangePriceDTO : exchangePriceDTOList){
                        if(StringUtils.isEmpty(coinPriceDTO.getTopExchangeName())){
                            coinPriceDTO.setTopExchangeName(exchangePriceDTO.getExchangeName());
                        }

                        QuotesSourceEnum quotesSource = exchangePriceDTO.getQuotesSource();

                        if(null == quotesSource){
                            continue;
                        }

                        QuotesDTO quotes = getQuotes(exchangePriceDTO.getQuotesSource(), chainCoin, contract, coin);

                        if(null != quotes){
                            BigDecimal lastCNYRate = quotes.getLastCNY();
                            BigDecimal lastUSDRate = quotes.getLastUSD();

                            exchangePriceDTO.setPriceCNYRate(lastCNYRate);
                            exchangePriceDTO.setPriceUSDRate(lastUSDRate);
                        }
                    }

                    coinPriceDTO.setExchangeList(exchangePriceDTOList);
                }

                CoinAvgPriceDTO avgPriceDTO = getAvgPrice(chainCoin, contract, coin);

                if(null != avgPriceDTO){
                    coinPriceDTO.setAvgUSDRate(avgPriceDTO.getAvgUSDRate());
                }else if(chainCoin.toUpperCase().equals(coin.toUpperCase())){
                    BigDecimal usdRate = cache.get(QuotesCache.PRICE_USD, coin.toUpperCase(), BigDecimal.class);
                    coinPriceDTO.setAvgUSDRate(usdRate);
                }else{
                    logger.warn("获取币种均价失败[{}:{}:{}:{}]", QuotesCache.AVG, chainCoin, contract, coin);
                }
            }
        }

        pageDTO.setRecords(priceDTOList);
        pageDTO.setPageTotal(page.getTotalPages());
        pageDTO.setTotal(page.getTotalElements());

        return pageDTO;
    }

    /**
     * 根据行情来源和币种获取行情信息
     * @param source 行情来源
     * @param chainCoin 主链币种
     * @param coin 币种
     * @return 行情
     */
    private QuotesDTO getQuotes(QuotesSourceEnum source, String chainCoin, String contract, String coin){
        String key = QuotesCache.QUOTES + QuotesCache.SEP + chainCoin + QuotesCache.SEP + contract + QuotesCache.SEP + coin;

        return cache.get(key, source.getName(), QuotesDTO.class);
    }

    /**
     * 获取交易对各交易所平均行情价
     * @param chainCoin 主链币种
     * @param contract 币种合约
     * @param coin 币种
     * @return
     */
    private CoinAvgPriceDTO getAvgPrice(String chainCoin, String contract, String coin){
        String key = QuotesCache.AVG + QuotesCache.SEP + chainCoin + QuotesCache.SEP + contract;

        return cache.get(key, coin, CoinAvgPriceDTO.class);
    }
}
