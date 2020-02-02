package org.wallet.service.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wallet.common.dto.application.AppChainDTO;
import org.wallet.common.entity.application.AppChainEntity;
import org.wallet.common.entity.application.AppTagEntity;
import org.wallet.common.enums.application.AppTagEnum;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.service.application.dao.AppChainJpaDao;
import org.wallet.service.application.dao.AppTagJpaDao;
import org.wallet.service.application.service.AppChainService;
import org.wallet.service.common.bind.specification.MySpecification;
import org.wallet.service.common.service.AbstractCrudService;

import java.util.List;

/**
 * @author zengfucheng
 **/
@Service
public class AppChainServiceImpl extends AbstractCrudService<AppChainJpaDao, AppChainEntity> implements AppChainService {

    @Autowired
    private AppTagJpaDao appTagJpaDao;

    @Override
    public List<AppChainDTO> findAdminAppChain() {
        List<AppChainDTO> chainDTOS = getRepository().findAdminAppChain();
        if(!CollectionUtils.isEmpty(chainDTOS)){
            chainDTOS.forEach(dto -> {
                Long tagAppCount = appTagJpaDao.count(new MySpecification<AppTagEntity>(Searchs.of(
                        SearchFilters.eq("chainId", dto.getId()),
                        SearchFilters.eq("tag", AppTagEnum.HOT)
                )).toPredicate());

                dto.setHotAppCount(tagAppCount);

            });
        }
        return chainDTOS;
    }
}
