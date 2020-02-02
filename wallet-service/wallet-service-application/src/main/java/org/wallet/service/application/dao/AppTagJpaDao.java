package org.wallet.service.application.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wallet.common.dto.application.AppTagDTO;
import org.wallet.common.dto.application.TagAppDTO;
import org.wallet.common.entity.application.AppTagEntity;
import org.wallet.service.common.dao.BaseRepository;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface AppTagJpaDao extends BaseRepository<AppTagEntity> {
    /**
     * 根据标签查询标签应用信息
     * @param tagDTO 标签
     * @return 标签应用信息
     */
    @Query("select new org.wallet.common.dto.application.TagAppDTO(tag.id, type.id, type.name, info.id, info.name, info.icon, info.url, tag.tag, tag.sort, tag.img) " +
            "from AppTagEntity tag " +
            "left join AppInfoEntity info on (tag.appId = info.id and tag.chainId = info.chainId) " +
            "left join AppTypeEntity type on (info.typeId = type.id and info.chainId = type.chainId) " +
            "where tag.chainId = :#{#tagDTO.chainId} " +
            "and tag.tag = :#{#tagDTO.tag} " +
            "order by tag.sort")
    List<TagAppDTO> findTagApp(@Param("tagDTO") AppTagDTO tagDTO);

    @Query("select new org.wallet.common.dto.application.TagAppDTO(tag.id, type.id, type.name, info.id, info.name, info.icon, info.url, tag.tag, tag.sort, tag.img) " +
            "from AppTagEntity tag " +
            "left join AppInfoEntity info on (tag.appId = info.id and tag.chainId = info.chainId) " +
            "left join AppTypeEntity type on (info.typeId = type.id and info.chainId = type.chainId) " +
            "where tag.chainId = :#{#tagDTO.chainId} " +
            "and tag.typeId = :#{#tagDTO.typeId} " +
            "and tag.tag = :#{#tagDTO.tag} " +
            "order by tag.sort")
    List<TagAppDTO> findTypeTagApp(@Param("tagDTO") AppTagDTO tagDTO);
}
