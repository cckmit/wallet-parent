package org.wallet.service.application.service;

import org.wallet.common.entity.TableExtAttrEntity;
import org.wallet.common.entity.TableExtAttrValueEntity;
import org.wallet.service.common.service.CrudService;

/**
 * @author zengfucheng
 **/
public interface TableExtAttrValueService extends CrudService<TableExtAttrValueEntity> {
    void updateAttr(TableExtAttrEntity entity);
}
