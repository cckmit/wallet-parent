package org.wallet.service.application.dao;

import org.springframework.data.jpa.repository.Query;
import org.wallet.common.dto.wallet.ExchangePriceDTO;
import org.wallet.common.entity.wallet.WalletCoinConfigEntity;
import org.wallet.service.common.dao.BaseRepository;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface WalletCoinConfigJpaDao extends BaseRepository<WalletCoinConfigEntity> {
    /**
     * 根据币种ID获取币种价格配置
     * @param coinId
     * @return
     */
    @Query("select new org.wallet.common.dto.wallet.ExchangePriceDTO(entity.id, exchange.name, exchange.quotesSource, entity.sort) " +
            "from WalletCoinConfigEntity entity " +
            "left join WalletExchangeEntity exchange on entity.exchangeId = exchange.id " +
            "where entity.enable = true " +
            "and entity.coinId = ?1 " +
            "and exchange.quotesSource is not null " +
            "order by entity.sort")
    List<ExchangePriceDTO> getExchangeList(Long coinId);
}
