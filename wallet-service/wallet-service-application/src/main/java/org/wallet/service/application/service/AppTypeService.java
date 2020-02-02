package org.wallet.service.application.service;

import org.wallet.common.dto.application.AppTypeDTO;
import org.wallet.common.entity.application.AppTypeEntity;
import org.wallet.service.common.service.CrudService;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface AppTypeService extends CrudService<AppTypeEntity> {
    /**
     * 获取推荐分类信息（后台）
     * @param appTypeDTO 分类
     * @return 信息
     */
    List<AppTypeDTO> findRecommend(AppTypeDTO appTypeDTO);

    /**
     * 获取推荐分类信息（API）
     * @param chainId 主链ID
     * @return 推荐分类
     */
    List<AppTypeDTO> findRecommendType(Long chainId);
}
