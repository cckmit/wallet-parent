package org.wallet.service.batch.dao;

import org.springframework.data.jpa.repository.Query;
import org.wallet.common.dto.wallet.ExchangePriceDTO;
import org.wallet.common.dto.wallet.WalletCoinPriceDTO;
import org.wallet.common.entity.wallet.WalletCoinConfigEntity;
import org.wallet.service.common.dao.BaseRepository;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface WalletCoinConfigJpaDao extends BaseRepository<WalletCoinConfigEntity> {
    /**
     * 根据币种ID获取币种价格配置
     * @param coinId 币种ID
     * @return 币种价格配置
     */
    @Query("select new org.wallet.common.dto.wallet.ExchangePriceDTO(entity.id, exchange.name, exchange.quotesSource, entity.sort) " +
            "from WalletCoinConfigEntity entity " +
            "left join WalletExchangeEntity exchange on entity.exchangeId = exchange.id " +
            "where entity.enable = true " +
            "and entity.coinId = ?1 " +
            "and exchange.quotesSource is not null " +
            "order by entity.sort")
    List<ExchangePriceDTO> getExchangeList(Long coinId);

    /**
     * 获取所有币种价格配置
     * @return 币种价格配置
     */
    @Query("select new org.wallet.common.dto.wallet.WalletCoinPriceDTO(chain.id, chain.coinName, coin.id, coin.contractAddress, coin.name) " +
            "from WalletCoinConfigEntity entity " +
            "left join WalletCoinEntity coin on entity.coinId = coin.id " +
            "left join AppChainEntity chain on entity.chainId = chain.id " +
            "where entity.enable = true " +
            "group by chain.id, chain.coinName, coin.id, coin.contractAddress, coin.name ")
    List<WalletCoinPriceDTO> findCoinAllPriceConfig();
}
