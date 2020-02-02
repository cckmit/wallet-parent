package org.wallet.service.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wallet.common.entity.TableExtAttrEntity;
import org.wallet.service.application.dao.TableExtAttrJpaDao;
import org.wallet.service.application.service.TableExtAttrService;
import org.wallet.service.application.service.TableExtAttrValueService;
import org.wallet.service.common.service.AbstractCrudService;

/**
 * @author zengfucheng
 **/
@Service
public class TableExtAttrServiceImpl extends AbstractCrudService<TableExtAttrJpaDao, TableExtAttrEntity> implements TableExtAttrService {

    @Autowired
    TableExtAttrValueService tableExtAttrValueService;

    @Override
    public TableExtAttrEntity save(TableExtAttrEntity tableExtAttrEntity) {
        boolean update = null != tableExtAttrEntity.getId();

        TableExtAttrEntity entity = super.save(tableExtAttrEntity);

        if(update){
            tableExtAttrValueService.updateAttr(entity);
        }

        return entity;
    }
}
