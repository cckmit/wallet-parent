package org.wallet.service.batch.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.CoinConstants;
import org.wallet.common.constants.cache.AppID;
import org.wallet.common.constants.cache.QuotesCache;
import org.wallet.common.constants.field.AppChainField;
import org.wallet.common.dto.block.req.QuotesReqDTO;
import org.wallet.common.dto.wallet.CoinAvgPriceDTO;
import org.wallet.common.dto.wallet.ExchangePriceDTO;
import org.wallet.common.dto.wallet.QuotesDTO;
import org.wallet.common.dto.wallet.WalletCoinPriceDTO;
import org.wallet.common.entity.application.AppChainEntity;
import org.wallet.common.enums.wallet.QuotesSourceEnum;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.dap.common.utils.ThreadPool;
import org.wallet.service.batch.dao.AppChainJpaDao;
import org.wallet.service.batch.dao.WalletCoinConfigJpaDao;
import org.wallet.service.batch.dao.WalletCoinJpaDao;
import org.wallet.service.batch.service.PriceService;
import org.wallet.service.common.bind.specification.MySpecification;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class PriceServiceImpl implements PriceService {

    @Autowired
    private Cache cache;

    @Autowired
    private AppChainJpaDao appChainJpaDao;

    @Autowired
    private WalletCoinJpaDao walletCoinJpaDao;

    @Autowired
    private WalletCoinConfigJpaDao walletCoinConfigJpaDao;

    @Reference(group = DubboServiceGroup.CLIENT_BINANCE)
    private IService binanceService;

    @Reference(group = DubboServiceGroup.CLIENT_QUOTES, timeout = 30000)
    private IService quotesService;

    @Override
    public void syncChainCoinUSDPrice() {
        List<AppChainEntity> chainList = appChainJpaDao.findAll(new MySpecification<AppChainEntity>(
                Searchs.of(SearchFilters.eq(AppChainField.ENABLE, Boolean.TRUE))
        ).toPredicate());

        if(!CollectionUtils.isEmpty(chainList)){
            Map<String, BigDecimal> usdPriceMap = getUSDPriceMap();
            if(CollectionUtils.isEmpty(usdPriceMap)){
                log.warn("获取美元汇率失败，跳过更新主链币种美元汇率");
                return;
            }
            for(AppChainEntity entity : chainList){
                String coinName = entity.getCoinName();
                if(StringUtils.isEmpty(coinName)){
                    continue;
                }
                coinName = coinName.toUpperCase();
                BigDecimal price = usdPriceMap.get(coinName);
                if(null != price && price.compareTo(BigDecimal.ZERO) > 0){
                    cache.put(QuotesCache.PRICE_USD, coinName, price);
                }
                log.info("更新主链币种[{}]美元汇率[{}]", coinName, price);
            }
        }
    }

    @Override
    public void syncAllCoinPrice() {
        List<WalletCoinPriceDTO> chainList = walletCoinConfigJpaDao.findCoinAllPriceConfig();

        if(!CollectionUtils.isEmpty(chainList)){
            for (WalletCoinPriceDTO price : chainList) {
                ThreadPool.getInstance().exe(() -> {
                    String chainCoin = price.getChainCoinName();
                    Long coinId = price.getCoinId();
                    String contract = price.getContract();
                    String coinName = price.getCoinName();

                    findAndUpdatePrice(chainCoin, contract, coinId, coinName);
                });
            }
        }
    }

    /**
     * 查询并更新币种美元汇率
     * @param chainCoin 主链
     * @param contract 合约
     * @param coinId 币种ID
     * @param coinName 币种名称
     */
    private void findAndUpdatePrice(String chainCoin, String contract, Long coinId, String coinName) {
        List<ExchangePriceDTO> priceList = walletCoinConfigJpaDao.getExchangeList(coinId);

        if(!CollectionUtils.isEmpty(priceList)){
            int sourceSize = priceList.size();
            Set<String> exchange = new HashSet<>();
            for(int i = 0; i < priceList.size(); i++){
                ExchangePriceDTO exchangePriceDTO = priceList.get(i);

                QuotesSourceEnum source = exchangePriceDTO.getQuotesSource();

                QuotesReqDTO quotesReqDTO = new QuotesReqDTO();

                quotesReqDTO.setSource(source);
                quotesReqDTO.setContract(contract);
                quotesReqDTO.setSymbol(coinName);
                quotesReqDTO.setAnchor(chainCoin);
                try{
                    String symbol = coinName + "/" + chainCoin;

                    if(coinName.toUpperCase().equals(chainCoin.toUpperCase())){ continue; }

                    ServiceRequest request = ServiceRequest.newInstance(AppID.WALLET_SERVICE_BATCH, DubboServiceGroup.CLIENT_QUOTES,
                            "findQuotes", null, quotesReqDTO);

                    ServiceResponse response = quotesService.invoke(request);

                    if(!response.success()){
                        log.warn("获取交易所[{}:{}]交易对[{}]行情失败[{}:{}]", source.name(), source.getLabel(),
                                symbol, response.getRespCode(), response.getRespMsg());

                        continue;
                    }

                    QuotesDTO quotes = response.getResult();

                    if(null == quotes){
                        log.warn("获取交易所[{}:{}]交易对[{}]行情结果为空", source.name(), source.getLabel(), symbol);

                        continue;
                    }

                    String quotesKey = QuotesCache.QUOTES + QuotesCache.SEP + chainCoin + QuotesCache.SEP +
                            contract + QuotesCache.SEP + coinName;

                    cache.put(quotesKey, source.name(), quotes);

                    exchange.add(source.name());
                } finally {
                    boolean lastSource = i == sourceSize - 1;

                    if(lastSource){
                        updateAvgPrice(exchange, quotesReqDTO);
                    }
                }
            }
        }
    }

    /**
     * 更新平均行情价
     * @param reqDTO 行情请求
     */
    @SuppressWarnings("unchecked")
    private void updateAvgPrice(Set<String> exchangeSet, QuotesReqDTO reqDTO){
        String symbol = reqDTO.getAnchor() + QuotesCache.SEP + reqDTO.getContract() + QuotesCache.SEP + reqDTO.getSymbol();

        if(!CollectionUtils.isEmpty(exchangeSet)){
            int size = 0;
            BigDecimal totalUSDRate = BigDecimal.ZERO;
            for(String exchange : exchangeSet){
                QuotesDTO quotes = getQuotes(reqDTO.getAnchor(), reqDTO.getSymbol(), reqDTO.getContract(), exchange);

                if(null != quotes){
                    size++;
                    totalUSDRate = totalUSDRate.add(quotes.getLastUSD());
                }
                log.info("币种[{}]交易所[{}]行情[{}]总美元汇率[{}]行情数量[{}]", symbol, exchange, JSON.toJSONString(quotes), totalUSDRate, size);
            }

            if(size > 0 && totalUSDRate.compareTo(BigDecimal.ZERO) > 0){
                BigDecimal avgUSDRate = totalUSDRate.divide(new BigDecimal(size), CoinConstants.SCALE_PRICE, BigDecimal.ROUND_DOWN);

                CoinAvgPriceDTO avgPriceDTO = new CoinAvgPriceDTO();
                avgPriceDTO.setCoinName(reqDTO.getSymbol());
                avgPriceDTO.setAvgUSDRate(avgUSDRate);

                String avgKey = QuotesCache.AVG + QuotesCache.SEP + reqDTO.getAnchor() + QuotesCache.SEP + reqDTO.getContract();
                cache.put(avgKey, reqDTO.getSymbol(), avgPriceDTO);
                String priceKey = QuotesCache.PRICE_USD + QuotesCache.SEP + reqDTO.getAnchor() + QuotesCache.SEP + reqDTO.getContract();
                cache.put(priceKey, reqDTO.getSymbol(), avgUSDRate);
                log.info("更新币种[{}:{}]美元汇率[{}] = [{} / {}]", priceKey, reqDTO.getSymbol(), avgUSDRate, totalUSDRate, size);
            }
        }
    }

    private QuotesDTO getQuotes(String chainCoin, String coinName, String contract, String source){
        String quotesKey = QuotesCache.QUOTES + QuotesCache.SEP + chainCoin + QuotesCache.SEP + contract + QuotesCache.SEP + coinName;
        return cache.get(quotesKey, source, QuotesDTO.class);
    }

    /**
     * 获取币种美元汇率Map
     * @return 美元汇率Map
     */
    private Map<String, BigDecimal> getUSDPriceMap(){
        ServiceResponse response = binanceService.invoke(ServiceRequest.newInstance(AppID.WALLET_SERVICE_BATCH,
                DubboServiceGroup.CLIENT_BINANCE, "getAllPrices"));

        if(response.success()){
            return response.getResultValue(CoinConstants.USDT);
        }

        return null;
    }
}
