package org.wallet.service.application.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.wallet.common.dto.wallet.WalletCoinPriceDTO;
import org.wallet.common.entity.wallet.WalletCoinEntity;
import org.wallet.service.common.dao.BaseRepository;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface WalletCoinJpaDao extends BaseRepository<WalletCoinEntity> {
    /**
     * 查询所有币种实体
     * @param chainId 主链ID
     * @return 币种
     */
    @Query("select new org.wallet.common.dto.wallet.WalletCoinPriceDTO(chain.id, chain.coinName, entity.id, entity.contractAddress, entity.name) " +
            "from WalletCoinEntity entity " +
            "left join AppChainEntity chain on entity.chainId = chain.id " +
            "where entity.enable = true " +
            "and entity.chainId = ?1 " +
            "order by entity.sort")
    Page<WalletCoinPriceDTO> findPriceDTOList(Long chainId, Pageable pageable);

    /**
     * 根据主链ID查询币种
     * @param chainId 主链ID
     * @param enable 是否可用
     * @return 币种信息
     */
    List<WalletCoinEntity> findByChainIdAndEnableOrderBySort(Long chainId, Boolean enable);

    /**
     * 根据主链ID 和 币种名称查询币种
     * @param chainId 主链ID
     * @param enable 是否可用
     * @param coinNames 币种名称
     * @return 币种信息
     */
    List<WalletCoinEntity> findByChainIdAndEnableAndNameInOrderBySort(Long chainId, Boolean enable, List<String> coinNames);
}
