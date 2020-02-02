package org.wallet.service.admin.service.impl;

import org.springframework.stereotype.Service;
import org.wallet.common.entity.admin.SysDictEntity;
import org.wallet.service.admin.dao.SysDictJpaDao;
import org.wallet.service.admin.service.SysDictService;
import org.wallet.service.common.service.AbstractCrudService;

/**
 * @author zengfucheng
 **/
@Service
public class SysDictServiceImpl extends AbstractCrudService<SysDictJpaDao, SysDictEntity> implements SysDictService {

}
