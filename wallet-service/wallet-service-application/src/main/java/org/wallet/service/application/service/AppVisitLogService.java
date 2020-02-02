package org.wallet.service.application.service;

import org.wallet.common.dto.application.req.FindAppVisitLogReqDTO;
import org.wallet.common.dto.chart.ChartDataDTO;
import org.wallet.common.dto.chart.RankDataDTO;
import org.wallet.common.entity.application.AppVisitLogEntity;
import org.wallet.service.common.service.CrudService;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface AppVisitLogService extends CrudService<AppVisitLogEntity> {
    /**
     * DApp访问统计
     * @param req 统计范围
     * @return 统计数据
     */
    List<ChartDataDTO> appVisitCount(FindAppVisitLogReqDTO req);

    /**
     * DApp访问排行
     * @param req 统计范围
     * @return 排行数据
     */
    List<RankDataDTO> appVisitRank(FindAppVisitLogReqDTO req);
}
