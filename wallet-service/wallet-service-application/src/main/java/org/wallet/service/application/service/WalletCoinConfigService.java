package org.wallet.service.application.service;

import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.wallet.WalletCoinPriceDTO;
import org.wallet.common.entity.wallet.WalletCoinConfigEntity;
import org.wallet.service.common.service.CrudService;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface WalletCoinConfigService extends CrudService<WalletCoinConfigEntity> {
    /**
     * 获取币种价格
     * @param chainId 主链ID
     * @return 币种价格
     */
    PageDTO<WalletCoinPriceDTO> findWalletCoinPrice(Long chainId, PageDTO<WalletCoinPriceDTO> pageDTO);
}
