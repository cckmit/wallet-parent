package org.wallet.service.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wallet.common.dto.application.*;
import org.wallet.common.entity.application.AppInfoEntity;
import org.wallet.common.enums.application.AppTagEnum;
import org.wallet.service.application.dao.AppInfoJpaDao;
import org.wallet.service.application.service.AppInfoService;
import org.wallet.service.application.service.AppTypeService;
import org.wallet.service.common.service.AbstractCrudService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengfucheng
 **/
@Service
public class AppInfoServiceImpl extends AbstractCrudService<AppInfoJpaDao, AppInfoEntity> implements AppInfoService {
    @Autowired
    private AppInfoService appInfoService;

    @Autowired
    private AppTypeService appTypeService;

    @Override
    public List<RecommendTypeDTO> findTypeRecommendApp(Long chainId) {
        List<AppTypeDTO> types = appTypeService.findRecommendType(chainId);

        List<RecommendTypeDTO> result = new ArrayList<>();

        if(!CollectionUtils.isEmpty(types)){
            types.forEach(type -> {
                RecommendTypeDTO recommendType = new RecommendTypeDTO();
                recommendType.setName(type.getName());
                recommendType.setIcon(type.getIcon());

                AppTagDTO tag = new AppTagDTO();

                tag.setChainId(chainId);
                tag.setTypeId(type.getId());
                tag.setTag(AppTagEnum.TYPE_RECOMMEND);

                List<AppInfoDTO> appList = appInfoService.findAppByTag(tag);

                if(!CollectionUtils.isEmpty(appList)){
                    recommendType.setApps(appList);
                }

                result.add(recommendType);
            });
        }

        return result;
    }

    @Override
    public List<AppInfoDTO> findAppByTag(AppTagDTO tag) {
        if(null != tag.getTypeId()){
            return getRepository().findApp(tag.getChainId(), tag.getTypeId(), tag.getTag());
        }else{
            return getRepository().findApp(tag.getChainId(), tag.getTag());
        }
    }
}
