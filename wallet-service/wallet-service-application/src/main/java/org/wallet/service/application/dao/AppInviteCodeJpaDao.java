package org.wallet.service.application.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.wallet.common.entity.application.AppInviteCodeEntity;
import org.wallet.common.enums.application.AppInviteCodeStatusEnum;
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

    @Modifying
    @Query("update AppInviteCodeEntity set status = ?2, refId = 0 where code = ?1")
    void rollbackCodeStatus(String code, AppInviteCodeStatusEnum status);
}
