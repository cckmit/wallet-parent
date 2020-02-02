package org.wallet.service.application.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wallet.common.constants.field.AppInfoField;
import org.wallet.common.constants.field.AppTagField;
import org.wallet.common.constants.field.AppTypeField;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.application.AppTypeDTO;
import org.wallet.common.entity.application.AppTypeEntity;
import org.wallet.common.enums.application.AppTagEnum;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.service.application.dao.AppTypeJpaDao;
import org.wallet.service.application.service.AppInfoService;
import org.wallet.service.application.service.AppTagService;
import org.wallet.service.application.service.AppTypeService;
import org.wallet.service.common.service.AbstractCrudService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengfucheng
 **/
@Service
public class AppTypeServiceImpl extends AbstractCrudService<AppTypeJpaDao, AppTypeEntity> implements AppTypeService {

    @Autowired
    private AppInfoService appInfoService;

    @Autowired
    private AppTagService appTagService;

    @Override
    public List<AppTypeDTO> findRecommend(AppTypeDTO appTypeDTO) {
        List<AppTypeEntity> typeEntities = findAll(Searchs.of(SortDTO.asc(AppTypeField.SORT),
                SearchFilters.eq(AppTypeField.APP_CHAIN_ID, appTypeDTO.getChainId())));

        if(CollectionUtils.isEmpty(typeEntities)){
            return null;
        }

        List<AppTypeDTO> result = new ArrayList<>();

        typeEntities.forEach(type -> {
            AppTypeDTO dto = new AppTypeDTO();

            BeanUtils.copyProperties(type, dto);

            Long appCount = appInfoService.getCount(Searchs.of(SearchFilters.eq(AppInfoField.APP_TYPE_ID, type.getId())));

            dto.setAppCount(appCount);

            Long recommendCount = appTagService.getCount(Searchs.of(
                    SearchFilters.eq(AppTagField.APP_TYPE_ID, type.getId()),
                    SearchFilters.eq(AppTagField.TAG, AppTagEnum.TYPE_RECOMMEND)
            ));

            dto.setRecommendCount(recommendCount);

            result.add(dto);
        });

        return result;
    }

    @Override
    public List<AppTypeDTO> findRecommendType(Long chainId) {
        return getRepository().findRecommendType(chainId, AppTagEnum.TYPE_RECOMMEND);
    }
}
