package org.wallet.gateway.client.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.domain.market.TickerPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.CoinConstants;
import org.wallet.dap.common.dubbo.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zengfucheng
 **/
@Service(group = DubboServiceGroup.CLIENT_BINANCE)
@org.springframework.stereotype.Service
public class BinanceService extends BaseDubboService implements IService {
    private Logger logger = LoggerFactory.getLogger(BinanceService.class);

    @Autowired
    private BinanceApiRestClient client;

    public ServiceResponse getAllPrices(ServiceRequest request, ServiceResponse response) {
        List<TickerPrice> allPrices = client.getAllPrices();

        if(!CollectionUtils.isEmpty(allPrices)){
            Map<String, BigDecimal> usdtPrice = new HashMap<>(allPrices.size());
            Map<String, BigDecimal> btcPrice = new HashMap<>(allPrices.size());
            Map<String, BigDecimal> ethPrice = new HashMap<>(allPrices.size());

            allPrices.forEach(ticker -> {
                String symbol = ticker.getSymbol();
                if(ticker.getSymbol().endsWith(CoinConstants.USDT)){
                    String coinName = symbol.substring(0, symbol.lastIndexOf(CoinConstants.USDT));
                    usdtPrice.put(coinName, new BigDecimal(ticker.getPrice()));
                }else if(ticker.getSymbol().endsWith(CoinConstants.BTC)){
                    String coinName = symbol.substring(0, symbol.lastIndexOf(CoinConstants.BTC));
                    btcPrice.put(coinName, new BigDecimal(ticker.getPrice()));
                }else if(ticker.getSymbol().endsWith(CoinConstants.ETH)){
                    String coinName = symbol.substring(0, symbol.lastIndexOf(CoinConstants.ETH));
                    ethPrice.put(coinName, new BigDecimal(ticker.getPrice()));
                }
            });
            response.setResultValue(CoinConstants.USDT, usdtPrice);
            response.setResultValue(CoinConstants.BTC, btcPrice);
            response.setResultValue(CoinConstants.ETH, ethPrice);
        }

        response.setRespCode(ResponseCode.SUCCESS);
        response.setRespMsg("成功");
        return response;
    }

    public ServiceResponse getAccount(ServiceRequest request, ServiceResponse response) {
        Account account = client.getAccount();

        response.setRespCode(ResponseCode.SUCCESS);
        response.setResultValue("account", account);
        response.setRespMsg("成功");
        return response;
    }

    public ServiceResponse newOrder(ServiceRequest request, ServiceResponse response) {
        NewOrder newOrder = NewOrder.marketBuy("BTCUSDT", "1");

        client.newOrderTest(newOrder);

        response.setRespCode(ResponseCode.SUCCESS);
        response.setRespMsg("成功");
        return response;
    }

    public ServiceResponse getKLineData(ServiceRequest request, ServiceResponse response) {
        String productCoinName = request.getParamValue("productCoinName");
        String priceCoinName = request.getParamValue("priceCoinName");
        CandlestickInterval interval = request.getParamValue("interval");
        Long startTime = request.getParamValue("startTime");
        Long endTime = request.getParamValue("endTime");
        Integer limit = request.getParamValue("limit");
        if(StringUtils.isEmpty(productCoinName)){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[productCoinName]不存在！");
            return response;
        }
        if(StringUtils.isEmpty(priceCoinName)){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[priceCoinName]不存在！");
            return response;
        }
        if(null == interval){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[interval]不存在！");
            return response;
        }
        String symbol = productCoinName + priceCoinName;
        boolean hasLimit = null != limit && limit > 0;
        boolean hasTime = startTime != null && endTime != null && endTime > startTime;
        List<Candlestick> candlesticks = null;
        if(hasLimit || hasTime){
            candlesticks = client.getCandlestickBars(symbol, interval, limit, startTime, endTime);
        }else{
            candlesticks = client.getCandlestickBars(symbol, interval);
        }

        response.setResultValue("list", candlesticks);

        response.setRespCode(ResponseCode.SUCCESS);
        response.setRespMsg("成功");
        return response;
    }
}
