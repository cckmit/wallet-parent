package org.wallet.gateway.client.config;

import com.binance.api.client.BinanceApiAsyncRestClient;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zengfucheng
 **/
@Configuration
public class BinanceConfig {

    @Value("${binance.api.key:}")
    private String apiKey;

    @Value("${binance.api.secret:}")
    private String secret;

    @Value("${binance.api.proxy.ip:}")
    private String proxyIP;

    @Value("${binance.api.proxy.port:0}")
    private Integer proxyPort;

    @Bean
    public BinanceApiRestClient binanceApiRestClient(){
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secret, proxyIP, proxyPort);
        BinanceApiRestClient client = factory.newRestClient();
        return client;
    }

    @Bean
    public BinanceApiAsyncRestClient binanceApiAsyncRestClient(){
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secret, proxyIP, proxyPort);
        BinanceApiAsyncRestClient client = factory.newAsyncRestClient();
        return client;
    }
}
