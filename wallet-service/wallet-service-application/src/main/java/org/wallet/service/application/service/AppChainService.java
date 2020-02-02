package org.wallet.service.application.service;

import org.wallet.common.dto.application.AppChainDTO;
import org.wallet.common.entity.application.AppChainEntity;
import org.wallet.service.common.service.CrudService;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface AppChainService extends CrudService<AppChainEntity> {
    /**
     * 查询主链
     * @return 主链
     */
    List<AppChainDTO> findAdminAppChain();
}
