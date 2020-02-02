package org.wallet.service.application.dao;

import org.springframework.data.jpa.repository.Query;
import org.wallet.common.dto.application.AppChainDTO;
import org.wallet.common.entity.application.AppChainEntity;
import org.wallet.service.common.dao.BaseRepository;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface AppChainJpaDao extends BaseRepository<AppChainEntity> {
    @Query("select new org.wallet.common.dto.application.AppChainDTO(chain.id, chain.coinName, chain.name, chain.intro, chain.icon, chain.sort, chain.enable, count(app.id)) " +
           "from AppChainEntity chain " +
           "left join AppInfoEntity app on app.chainId = chain.id " +
           "group by chain.id " +
           "order by chain.sort")
    List<AppChainDTO> findAdminAppChain();
}
