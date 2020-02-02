package org.wallet.service.batch.dao;

import org.wallet.common.entity.application.AppInviteCodeEntity;
import org.wallet.service.common.dao.BaseRepository;

/**
 * @author zengfucheng
 **/
public interface AppInviteCodeJpaDao extends BaseRepository<AppInviteCodeEntity> {
    /**
     * 根据邀请码获取实体
     * @param code 邀请码
     * @return 实体
     */
    AppInviteCodeEntity findByCode(String code);
}
