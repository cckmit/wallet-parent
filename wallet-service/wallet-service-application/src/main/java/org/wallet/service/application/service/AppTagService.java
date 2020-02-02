package org.wallet.service.application.service;

import org.wallet.common.dto.application.AppTagDTO;
import org.wallet.common.dto.application.TagAppDTO;
import org.wallet.common.entity.application.AppTagEntity;
import org.wallet.service.common.service.CrudService;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface AppTagService extends CrudService<AppTagEntity> {
    /**
     * 获取带标签的App信息
     * @param tag 标签
     * @return App信息
     */
    List<TagAppDTO> findTagApp(AppTagDTO tag);
}
