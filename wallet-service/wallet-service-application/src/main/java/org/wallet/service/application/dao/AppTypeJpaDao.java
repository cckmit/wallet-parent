package org.wallet.service.application.dao;

import org.springframework.data.jpa.repository.Query;
import org.wallet.common.dto.application.AppTypeDTO;
import org.wallet.common.entity.application.AppTypeEntity;
import org.wallet.common.enums.application.AppTagEnum;
import org.wallet.service.common.dao.BaseRepository;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface AppTypeJpaDao extends BaseRepository<AppTypeEntity> {
    @Query("select new org.wallet.common.dto.application.AppTypeDTO(id, name, icon, intro) from AppTypeEntity " +
            "where enable = true " +
            "and chainId = ?1" +
            "and id in (select typeId from AppTagEntity where chainId = ?1 and tag = ?2)" +
            "order by sort")
    List<AppTypeDTO> findRecommendType(Long chainId, AppTagEnum typeRecommend);
}
