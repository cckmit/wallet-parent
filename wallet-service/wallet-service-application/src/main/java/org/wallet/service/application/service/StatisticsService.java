package org.wallet.service.application.service;

import org.wallet.common.dto.admin.AssetsStatisticsDTO;

/**
 * @author zengfucheng
 **/
public interface StatisticsService {
    /**
     * 获取当日资产统计数据
     * @param chainId 主链ID
     * @return 资产统计数据实体
     */
    AssetsStatisticsDTO assetsStatistics(Long chainId);
}
