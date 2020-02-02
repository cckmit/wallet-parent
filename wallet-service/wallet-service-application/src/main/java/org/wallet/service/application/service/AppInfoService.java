package org.wallet.service.application.service;

import org.wallet.common.dto.application.AppInfoDTO;
import org.wallet.common.dto.application.AppTagDTO;
import org.wallet.common.dto.application.RecommendTypeDTO;
import org.wallet.common.entity.application.AppInfoEntity;
import org.wallet.service.common.service.CrudService;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface AppInfoService extends CrudService<AppInfoEntity> {
    /**
     * 查询分类推荐DApp
     * @param chainId 主链ID
     * @return DApp
     */
    List<RecommendTypeDTO> findTypeRecommendApp(Long chainId);

    /**
     * 根据标签获取DApp信息
     * @param tag 标签
     * @return DApp 信息
     */
    List<AppInfoDTO> findAppByTag(AppTagDTO tag);
}
