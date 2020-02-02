package org.wallet.service.application.dao;

import org.springframework.data.jpa.repository.Query;
import org.wallet.common.dto.application.AppInfoDTO;
import org.wallet.common.entity.application.AppInfoEntity;
import org.wallet.common.enums.application.AppTagEnum;
import org.wallet.service.common.dao.BaseRepository;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface AppInfoJpaDao extends BaseRepository<AppInfoEntity> {
    /**
     * 根据标签查询DApp信息
     * @param chainId 主链ID
     * @param tag 标签
     * @return DApp信息
     */
    @Query("select new org.wallet.common.dto.application.AppInfoDTO(app.id, app.name, app.intro, app.icon, app.url, tag.img) from AppTagEntity tag " +
            "left join AppInfoEntity app on tag.appId = app.id " +
            "where tag.chainId = ?1 and tag.tag = ?2 " +
            "order by tag.sort")
    List<AppInfoDTO> findApp(Long chainId, AppTagEnum tag);

    /**
     * 根据标签查询DApp信息
     * @param chainId 主链ID
     * @param typeId 类型ID
     * @param tag 标签
     * @return DApp信息
     */
    @Query("select new org.wallet.common.dto.application.AppInfoDTO(app.id, app.name, app.intro, app.icon, app.url, tag.img) from AppTagEntity tag " +
            "left join AppInfoEntity app on tag.appId = app.id " +
            "where tag.chainId = ?1 " +
            "and tag.typeId = ?2 " +
            "and tag.tag = ?3 " +
            "order by tag.sort")
    List<AppInfoDTO> findApp(Long chainId, Long typeId, AppTagEnum tag);
}
