package org.wallet.service.application.service;

import org.wallet.common.entity.application.AppChainNodeEntity;
import org.wallet.service.common.service.CrudService;

/**
 * @author zengfucheng
 **/
public interface AppChainNodeService extends CrudService<AppChainNodeEntity> {
    /**
     * 移除现有默认标识
     * @param chainId 主链ID
     */
    void removeDefault(Long chainId);
}
