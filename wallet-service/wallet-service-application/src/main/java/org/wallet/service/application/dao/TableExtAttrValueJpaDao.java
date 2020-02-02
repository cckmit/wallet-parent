package org.wallet.service.application.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wallet.common.entity.TableExtAttrEntity;
import org.wallet.common.entity.TableExtAttrValueEntity;
import org.wallet.service.common.dao.BaseRepository;

/**
 * @author zengfucheng
 **/
public interface TableExtAttrValueJpaDao extends BaseRepository<TableExtAttrValueEntity> {

    @Query("update TableExtAttrValueEntity set type = :#{#attr.type}, label = :#{#attr.label}, name = :#{#attr.name}, sort = :#{#attr.sort} where attrId = :#{#attr.id}")
    @Modifying
    void updateAttr(@Param("attr") TableExtAttrEntity attr);
}
