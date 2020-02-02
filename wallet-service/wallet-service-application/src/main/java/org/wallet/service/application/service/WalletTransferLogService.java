package org.wallet.service.application.service;

import org.wallet.common.dto.chart.ChartDataDTO;
import org.wallet.common.dto.wallet.req.TransferStatisticsReqDTO;
import org.wallet.common.entity.wallet.WalletTransferLogEntity;
import org.wallet.service.common.service.CrudService;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface WalletTransferLogService extends CrudService<WalletTransferLogEntity> {
    /**
     * 转账统计
     * @param reqDTO 转账统计请求
     * @return 图表数据
     */
    List<ChartDataDTO> transferStatistics(TransferStatisticsReqDTO reqDTO);
}
