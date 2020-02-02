package org.wallet.gateway.client.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.cache.QuotesCache;
import org.wallet.dap.cache.Cache;
import org.wallet.gateway.client.service.PriceService;

import java.math.BigDecimal;

/**
 * @author zengfucheng
 **/
@Service
public class PriceServiceImpl implements PriceService {

    @Autowired
    private Cache cache;

    @Override
    public BigDecimal getCoinUSDPrice(String coinName) {
        if(StringUtils.isEmpty(coinName)){ return null; }
        return cache.get(QuotesCache.PRICE_USD, coinName.toUpperCase(), BigDecimal.class);
    }
}
