package org.wallet.service.application.service.impl;

import org.springframework.stereotype.Service;
import org.wallet.common.entity.TableExtAttrEntity;
import org.wallet.common.entity.TableExtAttrValueEntity;
import org.wallet.service.application.dao.TableExtAttrValueJpaDao;
import org.wallet.service.application.service.TableExtAttrValueService;
import org.wallet.service.common.service.AbstractCrudService;

/**
 * @author zengfucheng
 **/
@Service
public class TableExtAttrValueServiceImpl extends AbstractCrudService<TableExtAttrValueJpaDao, TableExtAttrValueEntity> implements TableExtAttrValueService {
    @Override
    public void updateAttr(TableExtAttrEntity entity) {
        deleteEntityCache();

        getRepository().updateAttr(entity);
    }
}
