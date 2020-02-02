package org.wallet.service.application.service.impl;

import org.springframework.stereotype.Service;
import org.wallet.common.dto.application.AppTagDTO;
import org.wallet.common.dto.application.TagAppDTO;
import org.wallet.common.entity.application.AppTagEntity;
import org.wallet.common.enums.application.AppTagEnum;
import org.wallet.service.application.dao.AppTagJpaDao;
import org.wallet.service.application.service.AppTagService;
import org.wallet.service.common.service.AbstractCrudService;

import java.util.List;

/**
 * @author zengfucheng
 **/
@Service
public class AppTagServiceImpl extends AbstractCrudService<AppTagJpaDao, AppTagEntity> implements AppTagService {
    @Override
    public List<TagAppDTO> findTagApp(AppTagDTO tag) {
        AppTagEnum tagEnum = tag.getTag();
        Boolean typeTag = (tagEnum.equals(AppTagEnum.TYPE_RECOMMEND) || tagEnum.equals(AppTagEnum.FEATURED));
        if(typeTag){
            return getRepository().findTypeTagApp(tag);
        }else{
            return getRepository().findTagApp(tag);
        }
    }
}
