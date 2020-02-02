package org.wallet.service.application.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.wallet.common.entity.application.AppChainNodeEntity;
import org.wallet.service.common.dao.BaseRepository;

/**
 * @author zengfucheng
 **/
public interface AppChainNodeJpaDao extends BaseRepository<AppChainNodeEntity> {
    /**
     * 移除现有默认标识
     * @param chainId
     */
    @Modifying
    @Query("update AppChainNodeEntity set def = false where def = true and chainId = ?1")
    void removeDefault(Long chainId);
}
