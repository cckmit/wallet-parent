package org.wallet.service.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wallet.common.entity.admin.SysDeptEntity;
import org.wallet.service.admin.dao.SysDeptJpaDao;
import org.wallet.service.admin.dao.SysRoleDataScopeJpaDao;
import org.wallet.service.admin.service.SysDeptService;
import org.wallet.service.common.service.AbstractCrudService;

import java.util.List;

/**
 * @author zengfucheng
 **/
@Service
public class SysDeptServiceImpl extends AbstractCrudService<SysDeptJpaDao, SysDeptEntity> implements SysDeptService {

    @Autowired
    SysRoleDataScopeJpaDao dataScopeJpaDao;

    @Override
    public List<Long> findDataScopeList(Long userId) {
        return dataScopeJpaDao.findDataScopeList(userId);
    }
}
